package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.request.SchedulerSettingsRequestDTO;
import com.sosyalmedia.notificationservice.exception.InvalidCronExpressionException;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.SchedulerSettings;
import com.sosyalmedia.notificationservice.repository.SchedulerSettingsRepository;
import com.sosyalmedia.notificationservice.service.SchedulerSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerSettingsServiceImpl implements SchedulerSettingsService {

    private final SchedulerSettingsRepository schedulerSettingsRepository;

    @Override
    @Transactional
    public SchedulerSettings createSchedulerSetting(SchedulerSettingsRequestDTO requestDTO) {
        log.info("⚙️ Yeni scheduler ayarı oluşturuluyor: {}", requestDTO.getSettingName());

        // Setting key'in benzersiz olup olmadığını kontrol et
        if (schedulerSettingsRepository.existsBySettingKey(requestDTO.getSettingKey())) {
            throw new IllegalArgumentException("Bu setting key zaten kullanılıyor: " + requestDTO.getSettingKey());
        }

        // Cron expression geçerli mi kontrol et
        if (!isValidCronExpression(requestDTO.getCronExpression())) {
            throw InvalidCronExpressionException.forCronExpression(requestDTO.getCronExpression());
        }

        SchedulerSettings setting = SchedulerSettings.builder()
                .settingKey(requestDTO.getSettingKey())
                .settingName(requestDTO.getSettingName())
                .description(requestDTO.getDescription())
                .cronExpression(requestDTO.getCronExpression())
                .isActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : true)
                .build();

        SchedulerSettings saved = schedulerSettingsRepository.save(setting);
        log.info("✅ Scheduler ayarı oluşturuldu - ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public SchedulerSettings updateSchedulerSetting(Long id, SchedulerSettingsRequestDTO requestDTO) {
        log.info("⚙️ Scheduler ayarı güncelleniyor - ID: {}", id);

        SchedulerSettings setting = schedulerSettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerSettings", "id", id));

        // Setting key değişiyorsa, yeni key'in benzersiz olup olmadığını kontrol et
        if (!setting.getSettingKey().equals(requestDTO.getSettingKey())) {
            if (schedulerSettingsRepository.existsBySettingKey(requestDTO.getSettingKey())) {
                throw new IllegalArgumentException("Bu setting key zaten kullanılıyor: " + requestDTO.getSettingKey());
            }
        }

        // Cron expression geçerli mi kontrol et
        if (!isValidCronExpression(requestDTO.getCronExpression())) {
            throw InvalidCronExpressionException.forCronExpression(requestDTO.getCronExpression());
        }

        setting.setSettingKey(requestDTO.getSettingKey());
        setting.setSettingName(requestDTO.getSettingName());
        setting.setDescription(requestDTO.getDescription());
        setting.setCronExpression(requestDTO.getCronExpression());

        if (requestDTO.getIsActive() != null) {
            setting.setIsActive(requestDTO.getIsActive());
        }

        SchedulerSettings updated = schedulerSettingsRepository.save(setting);
        log.info("✅ Scheduler ayarı güncellendi - ID: {}", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteSchedulerSetting(Long id) {
        log.info("🗑️ Scheduler ayarı siliniyor - ID: {}", id);

        if (!schedulerSettingsRepository.existsById(id)) {
            throw new ResourceNotFoundException("SchedulerSettings", "id", id);
        }

        schedulerSettingsRepository.deleteById(id);
        log.info("✅ Scheduler ayarı silindi - ID: {}", id);
    }

    @Override
    @Transactional
    public SchedulerSettings toggleSchedulerActive(Long id) {
        log.info("🔄 Scheduler aktiflik durumu değiştiriliyor - ID: {}", id);

        SchedulerSettings setting = schedulerSettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerSettings", "id", id));

        setting.setIsActive(!setting.getIsActive());

        SchedulerSettings updated = schedulerSettingsRepository.save(setting);
        log.info("✅ Scheduler aktiflik durumu güncellendi - ID: {}, Durum: {}",
                id, updated.getIsActive() ? "Aktif" : "Pasif");

        return updated;
    }

    @Override
    @Transactional
    public SchedulerSettings updateCronExpression(Long id, String cronExpression) {
        log.info("⏰ Cron expression güncelleniyor - ID: {}, Cron: {}", id, cronExpression);

        SchedulerSettings setting = schedulerSettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerSettings", "id", id));

        // Cron expression geçerli mi kontrol et
        if (!isValidCronExpression(cronExpression)) {
            throw InvalidCronExpressionException.forCronExpression(cronExpression);
        }

        setting.setCronExpression(cronExpression);

        SchedulerSettings updated = schedulerSettingsRepository.save(setting);
        log.info("✅ Cron expression güncellendi - ID: {}", id);
        return updated;
    }

    @Override
    public SchedulerSettings getSchedulerSettingById(Long id) {
        log.debug("🔍 Scheduler ayarı getiriliyor - ID: {}", id);
        return schedulerSettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerSettings", "id", id));
    }

    @Override
    public SchedulerSettings getSchedulerSettingByKey(String settingKey) {
        log.debug("🔍 Scheduler ayarı getiriliyor - Key: {}", settingKey);
        return schedulerSettingsRepository.findBySettingKey(settingKey)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerSettings", "settingKey", settingKey));
    }

    @Override
    public List<SchedulerSettings> getAllSchedulerSettings() {
        log.debug("📋 Tüm scheduler ayarları getiriliyor");
        return schedulerSettingsRepository.findAll();
    }

    @Override
    public List<SchedulerSettings> getActiveSchedulerSettings() {
        log.debug("📋 Aktif scheduler ayarları getiriliyor");
        return schedulerSettingsRepository.findByIsActiveTrue();
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