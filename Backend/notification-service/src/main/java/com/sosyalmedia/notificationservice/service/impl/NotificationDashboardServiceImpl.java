package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.response.CompanyWithDeadlinesDTO;
import com.sosyalmedia.notificationservice.dto.response.NotificationDashboardResponse;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineResponse;
import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import com.sosyalmedia.notificationservice.mapper.PostDeadlineMapper;
import com.sosyalmedia.notificationservice.repository.PostDeadlineRepository;
import com.sosyalmedia.notificationservice.service.CustomerValidationService; // ✅ YENİ
import com.sosyalmedia.notificationservice.service.NotificationDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationDashboardServiceImpl implements NotificationDashboardService {

    private final PostDeadlineRepository repository;
    private final PostDeadlineMapper mapper;
    private final CustomerValidationService customerValidationService; // ✅ DEĞİŞTİ

    // ❌ KALDIRILDI: private final CustomerServiceClient customerServiceClient;

    @Override
    @Transactional(readOnly = true)
    public NotificationDashboardResponse getDashboardData() {
        return getDashboardData(7); // Varsayılan 7 gün
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationDashboardResponse getDashboardData(int days) {
        log.info("📊 Getting notification dashboard data for next {} days", days);

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        // 1. Verileri topla
        List<PostDeadline> upcomingDeadlines = repository.findUpcomingDeadlines(today, endDate);
        List<PostDeadline> overdueDeadlines = repository.findOverdueDeadlines(today);

        // 2. Firma bazlı gruplama
        List<CompanyWithDeadlinesDTO> companies = buildCompanyList(upcomingDeadlines);

        // 3. Kritik deadline'lar (0-1 gün)
        List<PostDeadlineResponse> criticalDeadlines = upcomingDeadlines.stream()
                .filter(d -> d.getDaysRemaining() <= 1)
                .map(this::toResponseWithCustomer)
                .sorted((a, b) -> Integer.compare(a.getDaysRemaining(), b.getDaysRemaining()))
                .collect(Collectors.toList());

        // 4. Gecikmiş deadline'lar
        List<PostDeadlineResponse> overdueResponses = overdueDeadlines.stream()
                .map(this::toResponseWithCustomer)
                .sorted((a, b) -> Integer.compare(a.getDaysRemaining(), b.getDaysRemaining())) // En eski en üstte
                .collect(Collectors.toList());

        // 5. İstatistikler
        NotificationDashboardResponse.DashboardStats stats = calculateStats(upcomingDeadlines, overdueDeadlines, companies);

        // 6. Response oluştur
        return NotificationDashboardResponse.builder()
                .stats(stats)
                .upcomingCompanies(companies)
                .criticalDeadlines(criticalDeadlines)
                .overdueDeadlines(overdueResponses)
                .build();
    }

    // ========== HELPER METHODS ==========

    /**
     * Firma listesi oluştur (deadline'lara göre grupla)
     */
    private List<CompanyWithDeadlinesDTO> buildCompanyList(List<PostDeadline> deadlines) {
        // Customer ID'ye göre grupla
        Map<Long, List<PostDeadline>> groupedByCustomer = deadlines.stream()
                .collect(Collectors.groupingBy(PostDeadline::getCustomerId));

        // Her customer için DTO oluştur
        return groupedByCustomer.entrySet().stream()
                .map(entry -> {
                    Long customerId = entry.getKey();
                    List<PostDeadline> customerDeadlines = entry.getValue();

                    // ✅ DÜZELTME: CustomerValidationService kullan
                    CustomerBasicDTO customer = customerValidationService.getCustomerSafely(customerId);

                    // ✅ YENİ: Customer null ise fallback
                    if (customer == null) {
                        customer = CustomerBasicDTO.builder()
                                .id(customerId)
                                .companyName("⚠️ Müşteri Bulunamadı (ID: " + customerId + ")")
                                .sector("Bilinmiyor")
                                .status("UNKNOWN")
                                .build();
                    }

                    // Final reference for lambda
                    CustomerBasicDTO finalCustomer = customer;

                    // Deadline'ları response'a çevir ve sırala (en yakın tarih önce)
                    List<PostDeadlineResponse> deadlineResponses = customerDeadlines.stream()
                            .map(deadline -> mapper.toResponse(deadline, finalCustomer))
                            .sorted((a, b) -> Integer.compare(a.getDaysRemaining(), b.getDaysRemaining()))
                            .collect(Collectors.toList());

                    // İstatistikler hesapla
                    long criticalCount = deadlineResponses.stream()
                            .filter(d -> d.getUrgencyLevel() == PostDeadline.UrgencyLevel.CRITICAL)
                            .count();

                    long warningCount = deadlineResponses.stream()
                            .filter(d -> d.getUrgencyLevel() == PostDeadline.UrgencyLevel.WARNING)
                            .count();

                    return CompanyWithDeadlinesDTO.builder()
                            .customerId(customerId)
                            .companyName(finalCustomer.getCompanyName())
                            .sector(finalCustomer.getSector())
                            .customerStatus(finalCustomer.getStatus())
                            .upcomingDeadlines(deadlineResponses)
                            .totalUpcomingCount(deadlineResponses.size())
                            .criticalCount((int) criticalCount)
                            .warningCount((int) warningCount)
                            .build();
                })
                .sorted((a, b) -> {
                    // Önce kritik sayısına göre (fazla olan üstte)
                    int criticalCompare = b.getCriticalCount().compareTo(a.getCriticalCount());
                    if (criticalCompare != 0) return criticalCompare;

                    // Sonra warning sayısına göre
                    int warningCompare = b.getWarningCount().compareTo(a.getWarningCount());
                    if (warningCompare != 0) return warningCompare;

                    // En son toplam deadline sayısına göre
                    return b.getTotalUpcomingCount().compareTo(a.getTotalUpcomingCount());
                })
                .collect(Collectors.toList());
    }

    /**
     * Dashboard istatistiklerini hesapla
     */
    private NotificationDashboardResponse.DashboardStats calculateStats(
            List<PostDeadline> upcomingDeadlines,
            List<PostDeadline> overdueDeadlines,
            List<CompanyWithDeadlinesDTO> companies) {

        // Urgency bazlı sayılar
        long criticalCount = upcomingDeadlines.stream()
                .filter(d -> d.getDaysRemaining() <= 1)
                .count();

        long warningCount = upcomingDeadlines.stream()
                .filter(d -> d.getDaysRemaining() >= 2 && d.getDaysRemaining() <= 3)
                .count();

        long normalCount = upcomingDeadlines.stream()
                .filter(d -> d.getDaysRemaining() >= 4 && d.getDaysRemaining() <= 7)
                .count();

        // Status bazlı sayılar
        long notStartedCount = upcomingDeadlines.stream()
                .filter(d -> d.getStatus() == PostDeadlineStatus.NOT_STARTED)
                .count();

        long inProgressCount = upcomingDeadlines.stream()
                .filter(d -> d.getStatus() == PostDeadlineStatus.IN_PROGRESS)
                .count();

        long readyCount = upcomingDeadlines.stream()
                .filter(d -> d.getStatus() == PostDeadlineStatus.READY)
                .count();

        // Content hazırlık durumu
        long contentReadyCount = upcomingDeadlines.stream()
                .filter(PostDeadline::getContentReady)
                .count();

        long contentNotReadyCount = upcomingDeadlines.stream()
                .filter(d -> !d.getContentReady())
                .count();

        return NotificationDashboardResponse.DashboardStats.builder()
                .totalUpcoming((long) upcomingDeadlines.size())
                .totalCompanies((long) companies.size())
                .criticalCount(criticalCount)
                .warningCount(warningCount)
                .normalCount(normalCount)
                .overdueCount((long) overdueDeadlines.size())
                .notStartedCount(notStartedCount)
                .inProgressCount(inProgressCount)
                .readyCount(readyCount)
                .contentReadyCount(contentReadyCount)
                .contentNotReadyCount(contentNotReadyCount)
                .build();
    }

    /**
     * Customer bilgisini güvenli şekilde çek
     */
    private CustomerBasicDTO getCustomerSafely(Long customerId) {
        // ✅ DÜZELTME: CustomerValidationService kullan
        CustomerBasicDTO customer = customerValidationService.getCustomerSafely(customerId);

        // ✅ YENİ: Customer null ise fallback
        if (customer == null) {
            log.warn("⚠️ Customer not found in dashboard: {}", customerId);
            return CustomerBasicDTO.builder()
                    .id(customerId)
                    .companyName("⚠️ Müşteri Bulunamadı (ID: " + customerId + ")")
                    .sector("Bilinmiyor")
                    .status("UNKNOWN")
                    .build();
        }

        return customer;
    }

    /**
     * Entity'yi customer ile birlikte response'a çevir
     */
    private PostDeadlineResponse toResponseWithCustomer(PostDeadline deadline) {
        CustomerBasicDTO customer = getCustomerSafely(deadline.getCustomerId());
        return mapper.toResponse(deadline, customer);
    }
}