// src/main/java/com/sosyalmedia/analytics/repository/ActivityLogRepository.java

package com.sosyalmedia.analytics.repository;

import com.sosyalmedia.analytics.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    // Belirli sayıda son aktiviteyi getir (tüm müşteriler)
    List<ActivityLog> findTop10ByOrderByCreatedAtDesc();

    // Customer ID'ye göre son aktiviteleri getir
    List<ActivityLog> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // Activity type'a göre filtrele
    List<ActivityLog> findByActivityTypeOrderByCreatedAtDesc(ActivityLog.ActivityType activityType);

    // Custom query: Son N aktiviteyi getir
    @Query(value = "SELECT * FROM activity_logs ORDER BY created_at DESC LIMIT ?1", nativeQuery = true)
    List<ActivityLog> findRecentActivities(int limit);

    // Custom query: Belirli bir müşterinin son N aktivitesini getir
    @Query(value = "SELECT * FROM activity_logs WHERE customer_id = ?1 ORDER BY created_at DESC LIMIT ?2", nativeQuery = true)
    List<ActivityLog> findRecentActivitiesByCustomerId(Long customerId, int limit);
}