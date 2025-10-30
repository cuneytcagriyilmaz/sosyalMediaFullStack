package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.dto.response.AutoScheduleResponse;
import com.sosyalmedia.notificationservice.service.AutoScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications/auto-schedule")
@RequiredArgsConstructor
@Slf4j
public class AutoScheduleController {

    private final AutoScheduleService autoScheduleService;

    /**
     * 🎯 Yeni müşteri için otomatik takvim oluştur
     * <p>
     * Customer oluşturulduktan sonra bu endpoint'i çağırarak:
     * - İlk post (1 ay sonra)
     * - 100 düzenli post (frequency bazlı)
     * - Özel günler (resmi tatiller)
     * <p>
     * otomatik olarak oluşturulur.
     */
    @PostMapping("/{customerId}")

    public ResponseEntity<ApiResponse<AutoScheduleResponse>> createAutoSchedule(
            @PathVariable Long customerId) {

        log.info("🎯 REST: POST /api/v1/notifications/auto-schedule/{}", customerId);

        AutoScheduleResponse response = autoScheduleService.createAutoSchedule(customerId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response.getMessage(), response));
    }
}