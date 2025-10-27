package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.SchedulerSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchedulerSettingsRepository extends JpaRepository<SchedulerSettings, Long> {

    /**
     * Setting key ile bul
     */
    Optional<SchedulerSettings> findBySettingKey(String settingKey);

    /**
     * Setting key var mı kontrol et
     */
    boolean existsBySettingKey(String settingKey);

    /**
     * Aktif ayarları getir
     */
    List<SchedulerSettings> findByIsActiveTrue();

    /**
     * Pasif ayarları getir
     */
    List<SchedulerSettings> findByIsActiveFalse();
}