// src/main/java/com/sosyalmedia/analytics/service/impl/DashboardServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.client.CustomerServiceClient;
import com.sosyalmedia.analytics.client.dto.customerservice.ApiResponseDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerListDTO;
import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.dto.DashboardStatsDTO;
import com.sosyalmedia.analytics.dto.PlatformStatsDTO;
import com.sosyalmedia.analytics.service.ActivityLogService;
import com.sosyalmedia.analytics.service.DashboardService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ActivityLogService activityLogService;
    private final CustomerServiceClient customerServiceClient;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        log.info("📊 Fetching dashboard statistics from Customer-Service");

        // ✅ 1. Customer-Service'den ÖZET LİSTEYİ ÇEK
        List<CustomerListDTO> allCustomers = fetchAllCustomersSummary();

        // ✅ 2. Müşteri İstatistiklerini Hesapla
        long totalCustomers = allCustomers.size();
        long activeCustomers = countByStatus(allCustomers, "ACTIVE");
        long pendingCustomers = countByStatus(allCustomers, "PASSIVE");
        long inactiveCustomers = countByStatus(allCustomers, "CANCELLED");

        log.info("📈 Customer Stats: Total={}, Active={}, Pending={}, Inactive={}",
                totalCustomers, activeCustomers, pendingCustomers, inactiveCustomers);

        // ✅ 3. Platform İstatistikleri (Mock - Post Service yokken)
        Map<String, PlatformStatsDTO> platformStats = getMockPlatformStats();

        // ✅ 4. Post İstatistikleri (Mock - Post Service yokken)
        long totalPostsGenerated = 1200L;
        long totalPostsSent = 850L;
        long totalPostsScheduled = 180L;
        long totalPostsReady = 170L;
        long thisMonthPosts = 95L;

        // ✅ 5. Bu Ay Yeni Müşteri Sayısı (Gerçek)
        long thisMonthNewCustomers = calculateThisMonthNewCustomers(allCustomers);

        // ✅ 6. Son Aktiviteler (Analytics DB'den)
        List<ActivityLogDTO> recentActivities = activityLogService.getRecentActivities(10);

        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .totalCustomers(totalCustomers)
                .activeCustomers(activeCustomers)
                .pendingCustomers(pendingCustomers)
                .inactiveCustomers(inactiveCustomers)
                .totalPostsGenerated(totalPostsGenerated)
                .totalPostsSent(totalPostsSent)
                .totalPostsScheduled(totalPostsScheduled)
                .totalPostsReady(totalPostsReady)
                .thisMonthPosts(thisMonthPosts)
                .thisMonthNewCustomers(thisMonthNewCustomers)
                .platformStats(platformStats)
                .recentActivities(recentActivities)
                .build();

        log.info("✅ Dashboard statistics fetched successfully - {} total customers", totalCustomers);
        return stats;
    }

    @Override
    public Map<String, PlatformStatsDTO> getPlatformStats() {
        log.info("📊 Fetching platform statistics");
        return getMockPlatformStats();
    }

    @Override
    public List<ActivityLogDTO> getRecentActivities(int limit) {
        log.info("📋 Fetching {} recent activities", limit);
        return activityLogService.getRecentActivities(limit);
    }

    // ========== HELPER METHODS ==========

    /**
     * Customer-Service'den TÜM müşterilerin özet listesini çek
     * ✅ HATA YÖNETİMİ EKLENDİ
     */
    private List<CustomerListDTO> fetchAllCustomersSummary() {
        try {
            log.debug("🔄 Fetching customers from Customer-Service...");

            ApiResponseDTO<List<CustomerListDTO>> response = customerServiceClient.getAllCustomers();

            if (response.isSuccess() && response.getData() != null) {
                log.info("✅ Fetched {} customers from Customer-Service", response.getData().size());
                return response.getData();
            }

            log.warn("⚠️ Customer-Service returned empty or unsuccessful response");
            return Collections.emptyList();

        } catch (FeignException.NotFound e) {
            log.error("❌ Customer-Service endpoint not found (404): {}", e.getMessage());
            return Collections.emptyList();

        } catch (FeignException.ServiceUnavailable e) {
            log.error("❌ Customer-Service unavailable (503): {}", e.getMessage());
            return Collections.emptyList();

        } catch (FeignException e) {
            log.error("❌ Feign error while fetching customers: {} - {}", e.status(), e.getMessage());
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("❌ Unexpected error while fetching customers: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Status'e göre müşteri sayısı
     */
    private long countByStatus(List<CustomerListDTO> customers, String status) {
        return customers.stream()
                .filter(c -> status.equals(c.getStatus()))
                .count();
    }

    /**
     * Bu ay yeni eklenen müşteri sayısı
     */
    private long calculateThisMonthNewCustomers(List<CustomerListDTO> customers) {
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        long count = customers.stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().isAfter(startOfMonth))
                .count();

        log.debug("📅 This month new customers: {}", count);
        return count;
    }

    /**
     * Platform İstatistikleri (Mock Data)
     * NOT: Gerçek veri için Customer-Service'den sosyal medya bilgileri çekilmeli
     * ama performans için şimdilik mock kullanıyoruz
     */
    private Map<String, PlatformStatsDTO> getMockPlatformStats() {
        Map<String, PlatformStatsDTO> platformStats = new HashMap<>();

        // Instagram
        platformStats.put("instagram", PlatformStatsDTO.builder()
                .connectedCustomers(10L)
                .totalPosts(480L)
                .sentPosts(380L)
                .scheduledPosts(60L)
                .readyPosts(40L)
                .color("pink")
                .build());

        // TikTok
        platformStats.put("tiktok", PlatformStatsDTO.builder()
                .connectedCustomers(8L)
                .totalPosts(180L)
                .sentPosts(150L)
                .scheduledPosts(20L)
                .readyPosts(10L)
                .color("black")
                .build());

        // Facebook
        platformStats.put("facebook", PlatformStatsDTO.builder()
                .connectedCustomers(6L)
                .totalPosts(420L)
                .sentPosts(250L)
                .scheduledPosts(80L)
                .readyPosts(90L)
                .color("blue")
                .build());

        // YouTube
        platformStats.put("youtube", PlatformStatsDTO.builder()
                .connectedCustomers(3L)
                .totalPosts(120L)
                .sentPosts(70L)
                .scheduledPosts(20L)
                .readyPosts(30L)
                .color("red")
                .build());

        log.debug("📊 Platform stats generated (mock data)");
        return platformStats;
    }
}