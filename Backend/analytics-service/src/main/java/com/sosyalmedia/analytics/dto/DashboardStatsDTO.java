// src/main/java/com/sosyalmedia/analytics/dto/DashboardStatsDTO.java

package com.sosyalmedia.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    // Genel istatistikler
    private Long totalCustomers;
    private Long activeCustomers;
    private Long pendingCustomers;
    private Long inactiveCustomers;

    // Post istatistikleri
    private Long totalPostsGenerated;
    private Long totalPostsSent;
    private Long totalPostsScheduled;
    private Long totalPostsReady;

    // Bu ay
    private Long thisMonthPosts;
    private Long thisMonthNewCustomers;

    // Platform istatistikleri
    private Map<String, PlatformStatsDTO> platformStats;

    // Son aktiviteler
    private List<ActivityLogDTO> recentActivities;
}