package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

    /**
     * Setting key ile bul
     */
    Optional<NotificationSettings> findBySettingKey(String settingKey);

    /**
     * Setting key var mı kontrol et
     */
    boolean existsBySettingKey(String settingKey);

    /**
     * Aktif ayarları getir
     */
    List<NotificationSettings> findByIsActiveTrue();

    /**
     * Pasif ayarları getir
     */
    List<NotificationSettings> findByIsActiveFalse();
}