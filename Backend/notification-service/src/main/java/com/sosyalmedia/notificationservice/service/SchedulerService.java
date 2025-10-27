package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.SchedulerUpdateDTO;
import com.sosyalmedia.notificationservice.model.SchedulerSettings;

import java.util.List;

public interface SchedulerService {

    /**
     * Tüm scheduler ayarlarını getir
     */
    List<SchedulerSettings> getAllSettings();

    /**
     * Aktif scheduler ayarlarını getir
     */
    List<SchedulerSettings> getActiveSettings();

    /**
     * Setting key'e göre getir
     */
    SchedulerSettings getSettingByKey(String settingKey);

    /**
     * Scheduler ayarını güncelle
     */
    SchedulerSettings updateSetting(String settingKey, SchedulerUpdateDTO updateDTO);

    /**
     * Scheduler'ı aktif/pasif yap
     */
    SchedulerSettings toggleSchedulerStatus(String settingKey);

    /**
     * Cron expression geçerli mi kontrol et
     */
    boolean isValidCronExpression(String cronExpression);
}