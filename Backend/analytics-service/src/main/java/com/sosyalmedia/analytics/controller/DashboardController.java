// src/main/java/com/sosyalmedia/analytics/controller/DashboardController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.dto.DashboardStatsDTO;
import com.sosyalmedia.analytics.dto.PlatformStatsDTO;
import com.sosyalmedia.analytics.dto.response.ApiResponse;
import com.sosyalmedia.analytics.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics/dashboard")
@RequiredArgsConstructor
@Slf4j
 public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Dashboard genel istatistiklerini getir
     * GET /api/v1/analytics/dashboard
     */
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        log.info("GET /api/v1/analytics/dashboard - Fetching dashboard statistics");

        DashboardStatsDTO stats = dashboardService.getDashboardStats();

        return ResponseEntity.ok(
                ApiResponse.success("Dashboard statistics fetched successfully", stats)
        );
    }

    /**
     * Platform istatistiklerini getir
     * GET /api/v1/analytics/dashboard/platform-stats
     */
    @GetMapping("/platform-stats")
    public ResponseEntity<ApiResponse<Map<String, PlatformStatsDTO>>> getPlatformStats() {
        log.info("GET /api/v1/analytics/dashboard/platform-stats - Fetching platform statistics");

        Map<String, PlatformStatsDTO> platformStats = dashboardService.getPlatformStats();

        return ResponseEntity.ok(
                ApiResponse.success("Platform statistics fetched successfully", platformStats)
        );
    }

    /**
     * Son aktiviteleri getir
     * GET /api/v1/analytics/dashboard/activities?limit=10
     */
    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("GET /api/v1/analytics/dashboard/activities - Fetching {} recent activities", limit);

        List<ActivityLogDTO> activities = dashboardService.getRecentActivities(limit);

        return ResponseEntity.ok(
                ApiResponse.success("Recent activities fetched successfully", activities)
        );
    }
}