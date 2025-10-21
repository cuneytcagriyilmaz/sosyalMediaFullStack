// src/main/java/com/sosyalmedia/analytics/controller/ActivityLogController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.dto.response.ApiResponse;
import com.sosyalmedia.analytics.entity.ActivityLog;
import com.sosyalmedia.analytics.service.ActivityLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    /**
     * Yeni aktivite logu oluştur
     * POST /api/v1/analytics/activities
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ActivityLogDTO>> createActivityLog(
            @RequestBody ActivityLogDTO activityLogDTO) {

        log.info("POST /api/v1/analytics/activities - Creating activity log");

        ActivityLogDTO created = activityLogService.createActivityLog(activityLogDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Aktivite logu oluşturuldu", created));
    }

    /**
     * ID'ye göre aktivite logu getir
     * GET /api/v1/analytics/activities/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityLogDTO>> getActivityLogById(
            @PathVariable Long id) {

        log.info("GET /api/v1/analytics/activities/{}", id);

        ActivityLogDTO activityLog = activityLogService.getActivityLogById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Aktivite logu getirildi", activityLog)
        );
    }

    /**
     * Son N aktiviteyi getir
     * GET /api/v1/analytics/activities?limit=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /api/v1/analytics/activities?limit={}", limit);

        List<ActivityLogDTO> activities = activityLogService.getRecentActivities(limit);

        return ResponseEntity.ok(
                ApiResponse.success("Aktiviteler getirildi", activities)
        );
    }

    /**
     * Müşterinin son N aktivitesini getir
     * GET /api/v1/analytics/activities/customer/{customerId}?limit=10
     */
    @GetMapping("/customer/{customerId}")

    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getCustomerActivities(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /api/v1/analytics/activities/customer/{}?limit={}", customerId, limit);

        List<ActivityLogDTO> activities = activityLogService.getRecentActivitiesByCustomerId(customerId, limit);

        return ResponseEntity.ok(
                ApiResponse.success("Müşteri aktiviteleri getirildi", activities)
        );
    }

    /**
     * Activity type'a göre aktiviteleri getir
     * GET /api/v1/analytics/activities/type/{activityType}
     */
    @GetMapping("/type/{activityType}")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getActivitiesByType(
            @PathVariable ActivityLog.ActivityType activityType) {

        log.info("GET /api/v1/analytics/activities/type/{}", activityType);

        List<ActivityLogDTO> activities = activityLogService.getActivitiesByType(activityType);

        return ResponseEntity.ok(
                ApiResponse.success("Aktiviteler getirildi", activities)
        );
    }

    /**
     * Aktivite logu sil
     * DELETE /api/v1/analytics/activities/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActivityLog(
            @PathVariable Long id) {

        log.info("DELETE /api/v1/analytics/activities/{}", id);

        activityLogService.deleteActivityLog(id);

        return ResponseEntity.ok(
                ApiResponse.success("Aktivite logu silindi", null)
        );
    }
}