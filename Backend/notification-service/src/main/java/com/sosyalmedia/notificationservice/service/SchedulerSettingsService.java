package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.SchedulerSettingsRequestDTO;
import com.sosyalmedia.notificationservice.model.SchedulerSettings;

import java.util.List;

public interface SchedulerSettingsService {

    /**
     * Yeni scheduler ayarı oluştur
     */
    SchedulerSettings createSchedulerSetting(SchedulerSettingsRequestDTO requestDTO);

    /**
     * Scheduler ayarını güncelle
     */
    SchedulerSettings updateSchedulerSetting(Long id, SchedulerSettingsRequestDTO requestDTO);

    /**
     * Scheduler ayarını sil
     */
    void deleteSchedulerSetting(Long id);

    /**
     * Scheduler'ı aktif/pasif yap
     */
    SchedulerSettings toggleSchedulerActive(Long id);

    /**
     * Cron expression güncelle
     */
    SchedulerSettings updateCronExpression(Long id, String cronExpression);

    /**
     * ID'ye göre scheduler ayarı getir
     */
    SchedulerSettings getSchedulerSettingById(Long id);

    /**
     * Setting key'e göre scheduler ayarı getir
     */
    SchedulerSettings getSchedulerSettingByKey(String settingKey);

    /**
     * Tüm scheduler ayarlarını getir
     */
    List<SchedulerSettings> getAllSchedulerSettings();

    /**
     * Aktif scheduler ayarlarını getir
     */
    List<SchedulerSettings> getActiveSchedulerSettings();

    /**
     * Cron expression geçerli mi kontrol et
     */
    boolean isValidCronExpression(String cronExpression);
}