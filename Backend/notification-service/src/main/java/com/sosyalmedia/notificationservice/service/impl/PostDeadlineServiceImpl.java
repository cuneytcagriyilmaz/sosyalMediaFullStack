package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.AnalyticsServiceClient;
import com.sosyalmedia.notificationservice.client.dto.ActivityLogDTO;
import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.request.PostDeadlineCreateRequest;
import com.sosyalmedia.notificationservice.dto.request.PostDeadlineUpdateRequest;
import com.sosyalmedia.notificationservice.dto.response.CompanyWithDeadlinesDTO;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineResponse;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineStatsResponse;
import com.sosyalmedia.notificationservice.entity.Platform;
import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import com.sosyalmedia.notificationservice.exception.PostDeadlineNotFoundException;
import com.sosyalmedia.notificationservice.mapper.PostDeadlineMapper;
import com.sosyalmedia.notificationservice.repository.PostDeadlineRepository;
import com.sosyalmedia.notificationservice.service.CustomerValidationService;
import com.sosyalmedia.notificationservice.service.PostDeadlineArchiveService;
import com.sosyalmedia.notificationservice.service.PostDeadlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeadlineServiceImpl implements PostDeadlineService {

    private final PostDeadlineRepository repository;
    private final PostDeadlineMapper mapper;
    private final CustomerValidationService customerValidationService;
    private final AnalyticsServiceClient analyticsServiceClient;
    private final PostDeadlineArchiveService archiveService;


    @Override
    @Transactional
    public PostDeadlineResponse createDeadline(PostDeadlineCreateRequest request) {
        log.info("📝 Creating post deadline for customer: {}, date: {}",
                request.getCustomerId(), request.getScheduledDate());

        CustomerBasicDTO customer = customerValidationService.validateAndGetCustomer(
                request.getCustomerId());

        PostDeadline deadline = mapper.toEntity(request);
        PostDeadline saved = repository.save(deadline);

        log.info("✅ Post deadline created: ID={}", saved.getId());

        try {
            createActivityLog(customer.getId(), "DEADLINE_CREATED",
                    String.format("%s için yeni post deadline oluşturuldu (%s)",
                            customer.getCompanyName(), request.getScheduledDate()), "📅");
        } catch (Exception e) {
            log.warn("⚠️ Activity log eklenemedi: {}", e.getMessage());
        }

        return mapper.toResponse(saved, customer);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDeadlineResponse getDeadlineById(Long id) {
        log.info("🔍 Getting deadline by ID: {}", id);

        PostDeadline deadline = repository.findById(id)
                .orElseThrow(() -> new PostDeadlineNotFoundException(id));

        CustomerBasicDTO customer = customerValidationService.getCustomerSafely(
                deadline.getCustomerId());

        if (customer == null) {
            log.warn("⚠️ Customer not found for deadline: {} (Customer ID: {})",
                    id, deadline.getCustomerId());
            return buildFallbackResponse(deadline);
        }

        return mapper.toResponse(deadline, customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDeadlineResponse> getAllDeadlines() {
        log.info("📋 Getting all deadlines");

        List<PostDeadline> deadlines = repository.findAllOrderByScheduledDateAsc();

        return deadlines.stream()
                .map(deadline -> {
                    CustomerBasicDTO customer = customerValidationService.getCustomerSafely(
                            deadline.getCustomerId());

                    if (customer == null) {
                        return buildFallbackResponse(deadline);
                    }

                    return mapper.toResponse(deadline, customer);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDeadlineResponse> getUpcomingDeadlines(int days) {
        log.info("📅 Getting upcoming deadlines for next {} days", days);

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        List<PostDeadline> deadlines = repository.findUpcomingDeadlines(today, endDate);

        return deadlines.stream()
                .map(this::toResponseWithCustomer)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDeadlineResponse> getDeadlinesByCustomerId(Long customerId) {
        log.info("👤 Getting deadlines for customer: {}", customerId);

        // ✅ DÜZELTME: CustomerValidationService kullan
        CustomerBasicDTO customer = customerValidationService.validateAndGetCustomer(customerId);

        List<PostDeadline> deadlines = repository.findByCustomerIdOrderByScheduledDateAsc(customerId);

        return deadlines.stream()
                .map(deadline -> mapper.toResponse(deadline, customer))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDeadlineResponse updateDeadline(Long id, PostDeadlineUpdateRequest request) {
        log.info("🔄 Updating deadline: {}", id);

        PostDeadline deadline = repository.findById(id)
                .orElseThrow(() -> new PostDeadlineNotFoundException(id));

        PostDeadlineStatus oldStatus = deadline.getStatus();

        if (request.getScheduledDate() != null) {
            deadline.setScheduledDate(request.getScheduledDate());
        }
        if (request.getStatus() != null) {
            deadline.setStatus(request.getStatus());
        }
        if (request.getContentReady() != null) {
            deadline.setContentReady(request.getContentReady());
        }
        if (request.getPostContent() != null) {
            deadline.setPostContent(request.getPostContent());
        }
        if (request.getPlatform() != null) {
            deadline.setPlatform(request.getPlatform());
        }

        PostDeadline updated = repository.save(deadline);

        if (request.getStatus() == PostDeadlineStatus.SENT && oldStatus != PostDeadlineStatus.SENT) {
            log.info("📦 Auto-archiving deadline (status changed to SENT): ID={}", id);

            try {
                // ✅ DÜZELTME: CustomerValidationService kullan
                CustomerBasicDTO customer = customerValidationService.getCustomerSafely(
                        updated.getCustomerId());

                // Customer null ise fallback
                if (customer == null) {
                    log.warn("⚠️ Customer not found during archive, using fallback");
                    customer = CustomerBasicDTO.builder()
                            .id(updated.getCustomerId())
                            .companyName("Bilinmeyen Müşteri")
                            .build();
                }

                try {
                    createActivityLog(customer.getId(), "DEADLINE_ARCHIVED",
                            String.format("%s için deadline arşivlendi (Gönderildi)",
                                    customer.getCompanyName()), "📦");
                } catch (Exception e) {
                    log.warn("⚠️ Activity log eklenemedi: {}", e.getMessage());
                }

                var archived = archiveService.archiveDeadline(id, "AUTO_SENT");

                return PostDeadlineResponse.builder()
                        .id(archived.getId())
                        .customerId(archived.getCustomerId())
                        .companyName(archived.getCompanyName())
                        .sector(archived.getSector())
                        .scheduledDate(archived.getScheduledDate())
                        .status(archived.getFinalStatus())
                        .statusDisplayName(archived.getStatusDisplayName())
                        .statusColorCode(archived.getStatusColorCode())
                        .contentReady(archived.getContentReady())
                        .postContent(archived.getPostContent())
                        .platform(archived.getPlatform())
                        .platformDisplayName(archived.getPlatform() != null ? archived.getPlatform().getDisplayName() : null)
                        .platformBrandColor(archived.getPlatform() != null ? archived.getPlatform().getBrandColor() : null)
                        .createdAt(archived.getOriginalCreatedAt())
                        .updatedAt(archived.getOriginalUpdatedAt())
                        .daysRemaining(calculateDaysRemaining(archived.getScheduledDate()))
                        .urgencyLevel(calculateUrgencyLevel(archived.getScheduledDate()))
                        .urgencyColor(calculateUrgencyLevel(archived.getScheduledDate()).getColorCode())
                        .urgencyDisplayName(calculateUrgencyLevel(archived.getScheduledDate()).getDisplayName())
                        .build();

            } catch (Exception e) {
                log.error("❌ Otomatik arşivleme başarısız: {}", e.getMessage());
                CustomerBasicDTO customer = customerValidationService.getCustomerSafely(updated.getCustomerId());

                if (customer == null) {
                    return buildFallbackResponse(updated);
                }

                return mapper.toResponse(updated, customer);
            }
        }

        log.info("✅ Deadline updated: ID={}", id);

        // ✅ DÜZELTME: CustomerValidationService kullan
        CustomerBasicDTO customer = customerValidationService.getCustomerSafely(updated.getCustomerId());

        if (customer == null) {
            return buildFallbackResponse(updated);
        }

        return mapper.toResponse(updated, customer);
    }

    @Override
    @Transactional
    public void deleteDeadline(Long id) {
        log.info("🗑️ Deleting deadline: {}", id);

        if (!repository.existsById(id)) {
            throw new PostDeadlineNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("✅ Deadline deleted: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDeadlineStatsResponse getStatistics() {
        log.info("📊 Getting deadline statistics");

        LocalDate today = LocalDate.now();
        LocalDate weekEnd = today.plusDays(7);

        return PostDeadlineStatsResponse.builder()
                .totalDeadlines(repository.count())
                .upcomingWeek(repository.countUpcomingWeek(today, weekEnd))
                .notStarted(repository.countByStatus(PostDeadlineStatus.NOT_STARTED))
                .inProgress(repository.countByStatus(PostDeadlineStatus.IN_PROGRESS))
                .ready(repository.countByStatus(PostDeadlineStatus.READY))
                .sent(repository.countByStatus(PostDeadlineStatus.SENT))
                .critical(repository.countUpcomingWeek(today, today.plusDays(1)))
                .overdue(repository.countOverdue(today))
                .avgDaysRemaining(repository.getAverageDaysRemaining())
                .build();
    }

    @Override
    @Transactional
    public PostDeadlineResponse autoCreateForNewCustomer(Long customerId) {
        log.info("🎉 Auto-creating first deadline for new customer: {}", customerId);

        // ✅ DÜZELTME: CustomerValidationService kullan
        CustomerBasicDTO customer = customerValidationService.validateAndGetCustomer(customerId);

        LocalDate scheduledDate = LocalDate.now().plusDays(30);

        PostDeadlineCreateRequest request = PostDeadlineCreateRequest.builder()
                .customerId(customerId)
                .scheduledDate(scheduledDate)
                .status(PostDeadlineStatus.NOT_STARTED)
                .contentReady(false)
                .platform(Platform.INSTAGRAM)
                .postContent("İlk post içeriği hazırlanıyor...")
                .build();

        return createDeadline(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyWithDeadlinesDTO> getCompaniesWithUpcomingDeadlines(int days) {
        log.info("🏢 Getting companies with upcoming deadlines for next {} days", days);

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        List<PostDeadline> deadlines = repository.findUpcomingDeadlines(today, endDate);

        Map<Long, List<PostDeadline>> groupedByCustomer = deadlines.stream()
                .collect(Collectors.groupingBy(PostDeadline::getCustomerId));

        return groupedByCustomer.entrySet().stream()
                .map(entry -> {
                    Long customerId = entry.getKey();
                    List<PostDeadline> customerDeadlines = entry.getValue();

                    // ✅ DÜZELTME: CustomerValidationService kullan
                    CustomerBasicDTO customer = customerValidationService.getCustomerSafely(customerId);

                    // Customer null ise fallback
                    if (customer == null) {
                        customer = CustomerBasicDTO.builder()
                                .id(customerId)
                                .companyName("⚠️ Müşteri Bulunamadı (ID: " + customerId + ")")
                                .sector("Bilinmiyor")
                                .status("UNKNOWN")
                                .build();
                    }

                    CustomerBasicDTO finalCustomer = customer;
                    List<PostDeadlineResponse> deadlineResponses = customerDeadlines.stream()
                            .map(deadline -> mapper.toResponse(deadline, finalCustomer))
                            .collect(Collectors.toList());

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
                    int criticalCompare = b.getCriticalCount().compareTo(a.getCriticalCount());
                    if (criticalCompare != 0) return criticalCompare;
                    return b.getTotalUpcomingCount().compareTo(a.getTotalUpcomingCount());
                })
                .collect(Collectors.toList());
    }

    // ========== HELPER METHODS ==========

    // ❌ KALDIRILDI: private CustomerBasicDTO validateAndGetCustomer()
    // ❌ KALDIRILDI: private CustomerBasicDTO getCustomerSafely()
    // ✅ Artık CustomerValidationService kullanılıyor

    private void createActivityLog(Long customerId, String activityType, String message, String icon) {
        try {
            ActivityLogDTO activityDTO = ActivityLogDTO.builder()
                    .customerId(customerId)
                    .activityType(activityType)
                    .message(message)
                    .icon(icon)
                    .build();

            analyticsServiceClient.createActivity(activityDTO);
            log.debug("✅ Activity log created: {}", activityType);
        } catch (Exception e) {
            log.warn("⚠️ Activity log oluşturulamadı: {}", e.getMessage());
        }
    }

    private PostDeadlineResponse toResponseWithCustomer(PostDeadline deadline) {
        CustomerBasicDTO customer = customerValidationService.getCustomerSafely(deadline.getCustomerId());

        if (customer == null) {
            return buildFallbackResponse(deadline);
        }

        return mapper.toResponse(deadline, customer);
    }

    private int calculateDaysRemaining(LocalDate scheduledDate) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), scheduledDate);
    }

    private PostDeadline.UrgencyLevel calculateUrgencyLevel(LocalDate scheduledDate) {
        int daysRemaining = calculateDaysRemaining(scheduledDate);

        if (daysRemaining < 0) {
            return PostDeadline.UrgencyLevel.OVERDUE;
        } else if (daysRemaining <= 1) {
            return PostDeadline.UrgencyLevel.CRITICAL;
        } else if (daysRemaining <= 3) {
            return PostDeadline.UrgencyLevel.WARNING;
        } else if (daysRemaining <= 7) {
            return PostDeadline.UrgencyLevel.NORMAL;
        } else {
            return PostDeadline.UrgencyLevel.DISTANT;
        }
    }

    private PostDeadlineResponse buildFallbackResponse(PostDeadline deadline) {
        PostDeadlineResponse response = mapper.toResponse(deadline);
        response.setCompanyName("⚠️ Müşteri Bulunamadı (ID: " + deadline.getCustomerId() + ")");
        response.setSector("Bilinmiyor");
        response.setCustomerStatus("UNKNOWN");
        return response;
    }
}