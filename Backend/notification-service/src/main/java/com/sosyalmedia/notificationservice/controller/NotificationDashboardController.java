package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.dto.response.NotificationDashboardResponse;
import com.sosyalmedia.notificationservice.service.NotificationDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications/dashboard")
@RequiredArgsConstructor
@Slf4j
public class NotificationDashboardController {

    private final NotificationDashboardService dashboardService;

    /**
     * Bildiri Dashboard'u - Tüm verileri tek seferde getirir
     *
     * Frontend için ana endpoint:
     * - Son 7 gündeki firmalar ve deadline'ları (en kritikten en normale sıralı)
     * - Kritik ve gecikmiş deadline'lar
     * - Genel istatistikler
     */
    @GetMapping
    public ResponseEntity<ApiResponse<NotificationDashboardResponse>> getDashboard() {
        log.info("📊 REST: GET /api/v1/notifications/dashboard");

        NotificationDashboardResponse dashboard = dashboardService.getDashboardData();

        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }

    /**
     * Özel gün sayısı ile dashboard
     *
     * @param days Kaç günlük deadline'lar getirilsin (varsayılan: 7)
     */
    @GetMapping("/custom")
    public ResponseEntity<ApiResponse<NotificationDashboardResponse>> getCustomDashboard(
            @RequestParam(defaultValue = "7") int days) {

        log.info("📊 REST: GET /api/v1/notifications/dashboard/custom?days={}", days);

        NotificationDashboardResponse dashboard = dashboardService.getDashboardData(days);

        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }
}