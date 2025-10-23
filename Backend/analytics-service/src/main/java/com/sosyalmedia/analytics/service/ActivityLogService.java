// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/service/ActivityLogService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ActivityLogService {

    /**
     * Yeni aktivite oluştur
     */
    ActivityLogDTO createActivity(ActivityLogDTO dto);

    /**
     * Son N aktiviteyi getir
     */
    List<ActivityLogDTO> getRecentActivities(int limit);

    /**
     * Müşteriye ait aktiviteleri getir
     */
    List<ActivityLogDTO> getCustomerActivities(Long customerId, int limit);

    /**
     * Müşteriye ait son aktiviteleri getir (CustomerAnalyticsService için)
     *
     * @deprecated Use {@link #getCustomerActivities(Long, int)} instead
     */
    @Deprecated
    default List<ActivityLogDTO> getRecentActivitiesByCustomerId(Long customerId, int limit) {
        return getCustomerActivities(customerId, limit);
    }

    /**
     * Aktivite tipine göre getir
     */
    List<ActivityLogDTO> getActivitiesByType(String activityType, int limit);

    /**
     * Tarih aralığına göre aktiviteleri getir
     */
    List<ActivityLogDTO> getActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate, int limit);

    /**
     * Müşteri ve tip kombinasyonu ile getir
     */
    List<ActivityLogDTO> getCustomerActivitiesByType(Long customerId, String activityType, int limit);

    /**
     * Aktivite tiplerinin sayılarını getir
     */
    Map<String, Long> getActivityTypeStats();

    /**
     * Aktiviteyi ID ile getir
     */
    ActivityLogDTO getActivityById(Long id);


    /**
     * Aktivite güncelle
     */
    ActivityLogDTO updateActivity(Long id, ActivityLogDTO activityDTO);

    /**
     * Aktivite sil
     */
    void deleteActivity(Long id);

    /**
     * Toplu aktivite kaydet
     */
    List<ActivityLogDTO> createActivities(List<ActivityLogDTO> activities);
}