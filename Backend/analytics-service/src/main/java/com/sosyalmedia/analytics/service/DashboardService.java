// src/main/java/com/sosyalmedia/analytics/service/DashboardService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.dto.DashboardStatsDTO;
import com.sosyalmedia.analytics.dto.PlatformStatsDTO;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    /**
     * Dashboard için genel istatistikleri getir
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * Platform bazlı istatistikleri getir
     */
    Map<String, PlatformStatsDTO> getPlatformStats();

    /**
     * Son aktiviteleri getir
     */
    List<ActivityLogDTO> getRecentActivities(int limit);
}