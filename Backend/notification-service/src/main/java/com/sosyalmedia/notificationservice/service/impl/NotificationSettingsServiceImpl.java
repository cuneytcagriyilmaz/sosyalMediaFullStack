package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.request.NotificationSettingsUpdateDTO;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.NotificationSettings;
import com.sosyalmedia.notificationservice.repository.NotificationSettingsRepository;
import com.sosyalmedia.notificationservice.service.NotificationSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSettingsServiceImpl implements NotificationSettingsService {

    private final NotificationSettingsRepository settingsRepository;

    @Override
    public List<NotificationSettings> getAllSettings() {
        return settingsRepository.findAll();
    }

    @Override
    public List<NotificationSettings> getActiveSettings() {
        return settingsRepository.findByIsActiveTrue();
    }

    @Override
    public NotificationSettings getSettingByKey(String settingKey) {
        return settingsRepository.findBySettingKey(settingKey)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationSettings", "settingKey", settingKey));
    }

    @Override
    @Transactional
    public NotificationSettings updateSetting(String settingKey, NotificationSettingsUpdateDTO updateDTO) {
        NotificationSettings setting = getSettingByKey(settingKey);

        log.info("⚙️ Bildirim ayarı güncelleniyor: {}", settingKey);

        // Validation: Hatırlatma günleri boş olmamalı
        if (updateDTO.getReminderDays() == null || updateDTO.getReminderDays().isEmpty()) {
            throw new IllegalArgumentException("Reminder days cannot be empty");
        }

        // Validation: Günler pozitif olmalı
        for (Integer day : updateDTO.getReminderDays()) {
            if (day <= 0) {
                throw new IllegalArgumentException("Reminder days must be positive numbers");
            }
        }

        setting.setReminderDays(updateDTO.getReminderDays());

        if (updateDTO.getIsActive() != null) {
            setting.setIsActive(updateDTO.getIsActive());
        }

        NotificationSettings updated = settingsRepository.save(setting);

        log.info("✅ Bildirim ayarı güncellendi: {} - Günler: {} - Aktif: {}",
                settingKey, updated.getReminderDays(), updated.getIsActive());

        return updated;
    }

    @Override
    @Transactional
    public NotificationSettings toggleSettingStatus(String settingKey) {
        NotificationSettings setting = getSettingByKey(settingKey);

        setting.setIsActive(!setting.getIsActive());

        NotificationSettings updated = settingsRepository.save(setting);

        log.info("🔄 Bildirim ayarı durumu değiştirildi: {} - Yeni durum: {}",
                settingKey, updated.getIsActive() ? "Aktif" : "Pasif");

        return updated;
    }

    @Override
    @Transactional
    public NotificationSettings updateReminderDays(String settingKey, List<Integer> reminderDays) {
        NotificationSettings setting = getSettingByKey(settingKey);

        log.info("📅 Hatırlatma günleri güncelleniyor: {} - Yeni günler: {}", settingKey, reminderDays);

        // Validation
        if (reminderDays == null || reminderDays.isEmpty()) {
            throw new IllegalArgumentException("Reminder days cannot be empty");
        }

        for (Integer day : reminderDays) {
            if (day <= 0) {
                throw new IllegalArgumentException("Reminder days must be positive numbers");
            }
        }

        setting.setReminderDays(reminderDays);

        NotificationSettings updated = settingsRepository.save(setting);

        log.info("✅ Hatırlatma günleri güncellendi: {}", settingKey);

        return updated;
    }
}