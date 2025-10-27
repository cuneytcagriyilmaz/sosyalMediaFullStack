package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.request.SchedulerUpdateDTO;
import com.sosyalmedia.notificationservice.exception.InvalidCronExpressionException;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.SchedulerSettings;
import com.sosyalmedia.notificationservice.repository.SchedulerSettingsRepository;
import com.sosyalmedia.notificationservice.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerSettingsRepository settingsRepository;

    @Override
    public List<SchedulerSettings> getAllSettings() {
        return settingsRepository.findAll();
    }

    @Override
    public List<SchedulerSettings> getActiveSettings() {
        return settingsRepository.findByIsActiveTrue();
    }

    @Override
    public SchedulerSettings getSettingByKey(String settingKey) {
        return settingsRepository.findBySettingKey(settingKey)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerSettings", "settingKey", settingKey));
    }

    @Override
    @Transactional
    public SchedulerSettings updateSetting(String settingKey, SchedulerUpdateDTO updateDTO) {
        SchedulerSettings setting = getSettingByKey(settingKey);

        log.info("⚙️ Scheduler ayarı güncelleniyor: {}", settingKey);

        // Cron expression geçerli mi kontrol et
        if (!isValidCronExpression(updateDTO.getCronExpression())) {
            throw InvalidCronExpressionException.forCronExpression(updateDTO.getCronExpression());
        }

        setting.setCronExpression(updateDTO.getCronExpression());

        if (updateDTO.getIsActive() != null) {
            setting.setIsActive(updateDTO.getIsActive());
        }

        SchedulerSettings updated = settingsRepository.save(setting);

        log.info("✅ Scheduler ayarı güncellendi: {} - Cron: {} - Aktif: {}",
                settingKey, updated.getCronExpression(), updated.getIsActive());

        return updated;
    }

    @Override
    @Transactional
    public SchedulerSettings toggleSchedulerStatus(String settingKey) {
        SchedulerSettings setting = getSettingByKey(settingKey);

        setting.setIsActive(!setting.getIsActive());

        SchedulerSettings updated = settingsRepository.save(setting);

        log.info("🔄 Scheduler durumu değiştirildi: {} - Yeni durum: {}",
                settingKey, updated.getIsActive() ? "Aktif" : "Pasif");

        return updated;
    }

    @Override
    public boolean isValidCronExpression(String cronExpression) {
        try {
            CronExpression.parse(cronExpression);
            return true;
        } catch (Exception e) {
            log.warn("⚠️ Geçersiz cron expression: {}", cronExpression);
            return false;
        }
    }
}