package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.NotificationSettingsUpdateDTO;
import com.sosyalmedia.notificationservice.model.NotificationSettings;

import java.util.List;

public interface NotificationSettingsService {

    /**
     * Tüm bildirim ayarlarını getir
     */
    List<NotificationSettings> getAllSettings();

    /**
     * Aktif bildirim ayarlarını getir
     */
    List<NotificationSettings> getActiveSettings();

    /**
     * Setting key'e göre getir
     */
    NotificationSettings getSettingByKey(String settingKey);

    /**
     * Bildirim ayarını güncelle
     */
    NotificationSettings updateSetting(String settingKey, NotificationSettingsUpdateDTO updateDTO);

    /**
     * Bildirim ayarını aktif/pasif yap
     */
    NotificationSettings toggleSettingStatus(String settingKey);

    /**
     * Hatırlatma günlerini güncelle
     */
    NotificationSettings updateReminderDays(String settingKey, List<Integer> reminderDays);
}