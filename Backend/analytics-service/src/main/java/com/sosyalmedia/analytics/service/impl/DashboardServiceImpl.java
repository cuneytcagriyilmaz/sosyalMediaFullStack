// src/main/java/com/sosyalmedia/analytics/service/impl/DashboardServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.dto.DashboardStatsDTO;
import com.sosyalmedia.analytics.dto.PlatformStatsDTO;
import com.sosyalmedia.analytics.service.ActivityLogService;
import com.sosyalmedia.analytics.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ActivityLogService activityLogService; // ✅ Bu kullanılacak
    // TODO: Customer Service'den veri çekmek için Feign Client eklenecek
    // private final CustomerServiceClient customerServiceClient;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        log.info("Fetching dashboard statistics");

        // ✅ DÜZELTİLDİ: activityLogService üzerinden çağır
        List<ActivityLogDTO> recentActivities = activityLogService.getRecentActivities(5);

        // TODO: Gerçek verileri customer-service'den ve diğer servislerden çek
        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .totalCustomers(12L)
                .activeCustomers(9L)
                .pendingCustomers(3L)
                .inactiveCustomers(0L)
                .totalPostsGenerated(1200L)
                .totalPostsSent(850L)
                .totalPostsScheduled(180L)
                .totalPostsReady(170L)
                .thisMonthPosts(95L)
                .thisMonthNewCustomers(2L)
                .platformStats(getPlatformStats())
                .recentActivities(recentActivities) // ✅ Burada kullan
                .build();

        log.info("Dashboard statistics fetched successfully");
        return stats;
    }

    @Override
    public Map<String, PlatformStatsDTO> getPlatformStats() {
        log.info("Fetching platform statistics");

        // TODO: Gerçek verileri database'den çek
        Map<String, PlatformStatsDTO> platformStats = new HashMap<>();

        platformStats.put("instagram", PlatformStatsDTO.builder()
                .connectedCustomers(10L)
                .totalPosts(480L)
                .sentPosts(380L)
                .scheduledPosts(60L)
                .readyPosts(40L)
                .color("pink")
                .build());

        platformStats.put("tiktok", PlatformStatsDTO.builder()
                .connectedCustomers(7L)
                .totalPosts(340L)
                .sentPosts(260L)
                .scheduledPosts(50L)
                .readyPosts(30L)
                .color("gray")
                .build());

        platformStats.put("facebook", PlatformStatsDTO.builder()
                .connectedCustomers(5L)
                .totalPosts(240L)
                .sentPosts(150L)
                .scheduledPosts(40L)
                .readyPosts(50L)
                .color("blue")
                .build());

        platformStats.put("youtube", PlatformStatsDTO.builder()
                .connectedCustomers(3L)
                .totalPosts(140L)
                .sentPosts(60L)
                .scheduledPosts(30L)
                .readyPosts(50L)
                .color("red")
                .build());

        log.info("Platform statistics fetched successfully");
        return platformStats;
    }

    @Override
    public List<ActivityLogDTO> getRecentActivities(int limit) {
        log.info("Fetching {} recent activities for dashboard", limit);

        // ✅ DÜZELTİLDİ: activityLogService üzerinden çağır
        return activityLogService.getRecentActivities(limit);
    }
}