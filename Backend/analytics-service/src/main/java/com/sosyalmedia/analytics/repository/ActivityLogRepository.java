// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/repository/ActivityLogRepository.java

package com.sosyalmedia.analytics.repository;

import com.sosyalmedia.analytics.entity.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    /**
     * Müşteriye ait aktiviteleri getir (sayfalı)
     */
    List<ActivityLog> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * Aktivite tipine göre getir (sayfalı)
     */
    List<ActivityLog> findByActivityType(ActivityLog.ActivityType activityType, Pageable pageable);

    /**
     * Müşteriye ait son 10 aktiviteyi getir
     */
    List<ActivityLog> findTop10ByCustomerIdOrderByCreatedAtDesc(Long customerId);

    /**
     * Tarih aralığına göre aktiviteleri getir
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<ActivityLog> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Müşteri ve tip kombinasyonu ile getir
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.customerId = :customerId AND a.activityType = :activityType ORDER BY a.createdAt DESC")
    List<ActivityLog> findByCustomerIdAndActivityType(
            @Param("customerId") Long customerId,
            @Param("activityType") ActivityLog.ActivityType activityType,
            Pageable pageable
    );

    /**
     * Aktivite sayısını tip bazında getir
     */
    @Query("SELECT a.activityType, COUNT(a) FROM ActivityLog a GROUP BY a.activityType")
    List<Object[]> countByActivityType();
}