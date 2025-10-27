package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.response.DashboardSummaryDTO;
import com.sosyalmedia.notificationservice.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Genel dashboard özeti
     * GET /api/dashboard/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        log.info("📊 Dashboard özeti istendi");
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    /**
     * Müşteriye özel dashboard özeti
     * GET /api/dashboard/summary/customer/{customerId}
     */
    @GetMapping("/summary/customer/{customerId}")
    public ResponseEntity<DashboardSummaryDTO> getCustomerDashboardSummary(
            @PathVariable Long customerId
    ) {
        log.info("📊 Müşteri dashboard özeti istendi - Customer ID: {}", customerId);
        DashboardSummaryDTO summary = dashboardService.getCustomerDashboardSummary(customerId);
        return ResponseEntity.ok(summary);
    }
}