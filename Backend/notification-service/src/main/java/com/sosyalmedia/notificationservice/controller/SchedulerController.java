package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.request.SchedulerSettingsRequestDTO;
import com.sosyalmedia.notificationservice.dto.response.SchedulerSettingsDTO;
import com.sosyalmedia.notificationservice.model.SchedulerSettings;
import com.sosyalmedia.notificationservice.service.SchedulerSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
@Slf4j
public class SchedulerController {

    private final SchedulerSettingsService schedulerSettingsService;

    // ==================== CREATE ====================

    /**
     * Yeni scheduler ayarı oluştur
     * POST /api/scheduler/settings
     */
    @PostMapping("/settings")
    public ResponseEntity<SchedulerSettingsDTO> createSchedulerSetting(
            @Valid @RequestBody SchedulerSettingsRequestDTO request
    ) {
        log.info("⚙️ Yeni scheduler ayarı oluşturuluyor: {}", request.getSettingName());
        SchedulerSettings setting = schedulerSettingsService.createSchedulerSetting(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(setting));
    }

    // ==================== UPDATE ====================

    /**
     * Scheduler ayarını güncelle
     * PUT /api/scheduler/settings/{id}
     */
    @PutMapping("/settings/{id}")
    public ResponseEntity<SchedulerSettingsDTO> updateSchedulerSetting(
            @PathVariable Long id,
            @Valid @RequestBody SchedulerSettingsRequestDTO request
    ) {
        log.info("⚙️ Scheduler ayarı güncelleniyor - ID: {}", id);
        SchedulerSettings setting = schedulerSettingsService.updateSchedulerSetting(id, request);
        return ResponseEntity.ok(convertToDTO(setting));
    }

    /**
     * Scheduler ayarını aktif/pasif yap
     * PUT /api/scheduler/settings/{id}/toggle-active
     */
    @PutMapping("/settings/{id}/toggle-active")
    public ResponseEntity<SchedulerSettingsDTO> toggleSchedulerActive(@PathVariable Long id) {
        log.info("🔄 Scheduler aktiflik durumu değiştiriliyor - ID: {}", id);
        SchedulerSettings setting = schedulerSettingsService.toggleSchedulerActive(id);
        return ResponseEntity.ok(convertToDTO(setting));
    }

    /**
     * Cron expression güncelle
     * PUT /api/scheduler/settings/{id}/cron
     */
    @PutMapping("/settings/{id}/cron")
    public ResponseEntity<SchedulerSettingsDTO> updateCronExpression(
            @PathVariable Long id,
            @RequestParam String cronExpression
    ) {
        log.info("⏰ Cron expression güncelleniyor - ID: {}, Cron: {}", id, cronExpression);
        SchedulerSettings setting = schedulerSettingsService.updateCronExpression(id, cronExpression);
        return ResponseEntity.ok(convertToDTO(setting));
    }

    // ==================== DELETE ====================

    /**
     * Scheduler ayarını sil
     * DELETE /api/scheduler/settings/{id}
     */
    @DeleteMapping("/settings/{id}")
    public ResponseEntity<Void> deleteSchedulerSetting(@PathVariable Long id) {
        log.info("🗑️ Scheduler ayarı siliniyor - ID: {}", id);
        schedulerSettingsService.deleteSchedulerSetting(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GET ====================

    /**
     * ID'ye göre scheduler ayarı getir
     * GET /api/scheduler/settings/{id}
     */
    @GetMapping("/settings/{id}")
    public ResponseEntity<SchedulerSettingsDTO> getSchedulerSettingById(@PathVariable Long id) {
        log.info("🔍 Scheduler ayarı getiriliyor - ID: {}", id);
        SchedulerSettings setting = schedulerSettingsService.getSchedulerSettingById(id);
        return ResponseEntity.ok(convertToDTO(setting));
    }

    /**
     * Setting key'e göre scheduler ayarı getir
     * GET /api/scheduler/settings/key/{settingKey}
     */
    @GetMapping("/settings/key/{settingKey}")
    public ResponseEntity<SchedulerSettingsDTO> getSchedulerSettingByKey(@PathVariable String settingKey) {
        log.info("🔍 Scheduler ayarı getiriliyor - Key: {}", settingKey);
        SchedulerSettings setting = schedulerSettingsService.getSchedulerSettingByKey(settingKey);
        return ResponseEntity.ok(convertToDTO(setting));
    }

    /**
     * Tüm scheduler ayarlarını getir
     * GET /api/scheduler/settings
     */
    @GetMapping("/settings")
    public ResponseEntity<List<SchedulerSettingsDTO>> getAllSchedulerSettings() {
        log.info("📋 Tüm scheduler ayarları getiriliyor");
        List<SchedulerSettings> settings = schedulerSettingsService.getAllSchedulerSettings();
        return ResponseEntity.ok(convertToDTOs(settings));
    }

    /**
     * Aktif scheduler ayarlarını getir
     * GET /api/scheduler/settings/active
     */
    @GetMapping("/settings/active")
    public ResponseEntity<List<SchedulerSettingsDTO>> getActiveSchedulerSettings() {
        log.info("📋 Aktif scheduler ayarları getiriliyor");
        List<SchedulerSettings> settings = schedulerSettingsService.getActiveSchedulerSettings();
        return ResponseEntity.ok(convertToDTOs(settings));
    }

    // ==================== STATUS ====================

    /**
     * Scheduler durumunu kontrol et
     * GET /api/scheduler/status
     */
    @GetMapping("/status")
    public ResponseEntity<SchedulerStatusResponse> getSchedulerStatus() {
        log.info("📊 Scheduler durumu kontrol ediliyor");

        List<SchedulerSettings> allSettings = schedulerSettingsService.getAllSchedulerSettings();
        List<SchedulerSettings> activeSettings = schedulerSettingsService.getActiveSchedulerSettings();

        SchedulerStatusResponse status = SchedulerStatusResponse.builder()
                .totalSchedulers(allSettings.size())
                .activeSchedulers(activeSettings.size())
                .inactiveSchedulers(allSettings.size() - activeSettings.size())
                .schedulers(convertToDTOs(allSettings))
                .build();

        return ResponseEntity.ok(status);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private SchedulerSettingsDTO convertToDTO(SchedulerSettings setting) {
        return SchedulerSettingsDTO.builder()
                .id(setting.getId())
                .settingKey(setting.getSettingKey())
                .settingName(setting.getSettingName())
                .description(setting.getDescription())
                .cronExpression(setting.getCronExpression())
                .isActive(setting.getIsActive())
                .lastExecutedAt(setting.getLastExecutedAt())
                .nextExecutionAt(setting.getNextExecutionAt())
                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }

    private List<SchedulerSettingsDTO> convertToDTOs(List<SchedulerSettings> settings) {
        return settings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Inner class for status response
    @lombok.Data
    @lombok.Builder
    public static class SchedulerStatusResponse {
        private Integer totalSchedulers;
        private Integer activeSchedulers;
        private Integer inactiveSchedulers;
        private List<SchedulerSettingsDTO> schedulers;
    }
}