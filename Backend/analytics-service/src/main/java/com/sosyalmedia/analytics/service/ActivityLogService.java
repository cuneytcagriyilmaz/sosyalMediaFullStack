// src/main/java/com/sosyalmedia/analytics/service/ActivityLogService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.entity.ActivityLog;

import java.util.List;

public interface ActivityLogService {

    /**
     * Yeni aktivite logu oluştur
     */
    ActivityLogDTO createActivityLog(ActivityLogDTO activityLogDTO);

    /**
     * Aktivite logu getir
     */
    ActivityLogDTO getActivityLogById(Long id);

    /**
     * Belirli sayıda son aktiviteyi getir (tüm müşteriler)
     */
    List<ActivityLogDTO> getRecentActivities(int limit);

    /**
     * Belirli bir müşterinin son aktivitelerini getir
     */
    List<ActivityLogDTO> getRecentActivitiesByCustomerId(Long customerId, int limit);

    /**
     * Activity type'a göre aktiviteleri getir
     */
    List<ActivityLogDTO> getActivitiesByType(ActivityLog.ActivityType activityType);

    /**
     * Aktivite sil
     */
    void deleteActivityLog(Long id);
}