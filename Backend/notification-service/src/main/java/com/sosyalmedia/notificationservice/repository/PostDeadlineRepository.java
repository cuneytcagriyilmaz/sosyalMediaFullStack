package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostDeadlineRepository extends JpaRepository<PostDeadline, Long> {

    @Query("SELECT pd FROM PostDeadline pd ORDER BY pd.scheduledDate ASC")
    List<PostDeadline> findAllOrderByScheduledDateAsc();

    List<PostDeadline> findByCustomerIdOrderByScheduledDateAsc(Long customerId);

    List<PostDeadline> findByScheduledDateBetweenOrderByScheduledDateAsc(
            LocalDate startDate, LocalDate endDate);

    @Query("SELECT pd FROM PostDeadline pd WHERE pd.scheduledDate BETWEEN :today AND :endDate ORDER BY pd.scheduledDate ASC, pd.customerId ASC")
    List<PostDeadline> findUpcomingDeadlines(
            @Param("today") LocalDate today,
            @Param("endDate") LocalDate endDate);

    List<PostDeadline> findByStatus(PostDeadlineStatus status);

    List<PostDeadline> findByCustomerIdAndStatus(Long customerId, PostDeadlineStatus status);

    @Query("SELECT pd FROM PostDeadline pd WHERE pd.scheduledDate < :today AND pd.status NOT IN ('SENT', 'CANCELLED') ORDER BY pd.scheduledDate ASC")
    List<PostDeadline> findOverdueDeadlines(@Param("today") LocalDate today);

    long countByStatus(PostDeadlineStatus status);

    @Query("SELECT COUNT(pd) FROM PostDeadline pd WHERE pd.scheduledDate BETWEEN :today AND :endDate")
    long countUpcomingWeek(@Param("today") LocalDate today, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(pd) FROM PostDeadline pd WHERE pd.scheduledDate < :today AND pd.status NOT IN ('SENT', 'CANCELLED')")
    long countOverdue(@Param("today") LocalDate today);

    @Query("SELECT AVG(CAST(pd.scheduledDate - CURRENT_DATE AS double)) FROM PostDeadline pd WHERE pd.scheduledDate >= CURRENT_DATE")
    Double getAverageDaysRemaining();

    // ========== ✅ AUTO SCHEDULE METHOD'LARI ==========

    /**
     * Customer ve tarih bazlı arama (duplicate kontrolü için)
     */
    Optional<PostDeadline> findByCustomerIdAndScheduledDate(Long customerId, LocalDate scheduledDate);

    /**
     * Customer'ın otomatik oluşturulmuş deadline'larını say
     */
    @Query("SELECT COUNT(p) FROM PostDeadline p WHERE p.customerId = :customerId AND p.autoCreated = true")
    Long countAutoCreatedByCustomerId(@Param("customerId") Long customerId);

    /**
     * Customer'ın tüm otomatik deadline'larını sil (re-schedule için)
     */
    @Modifying
    @Query("DELETE FROM PostDeadline p WHERE p.customerId = :customerId AND p.autoCreated = true")
    void deleteAutoCreatedByCustomerId(@Param("customerId") Long customerId);

    /**
     * Event type'a göre say
     */
    Long countByEventType(PostDeadline.EventType eventType);

    // ========== ✅ NOTIFICATION SCHEDULER METHOD'LARI ==========

    /**
     * Belirli tarihte bildirim gönderilmemiş deadline'ları getir
     */
    @Query("""
        SELECT p FROM PostDeadline p 
        WHERE p.scheduledDate = :date
        AND p.notificationSent = false
        AND p.status NOT IN ('SENT', 'CANCELLED')
    """)
    List<PostDeadline> findByScheduledDateAndNotNotificationSent(@Param("date") LocalDate date);

    /**
     * Belirli tarihten eski deadline'ları getir (arşivleme için)
     */
    @Query("""
        SELECT p FROM PostDeadline p 
        WHERE p.scheduledDate < :cutoffDate
        AND p.status IN ('SENT', 'CANCELLED')
        ORDER BY p.scheduledDate ASC
    """)
    List<PostDeadline> findExpiredDeadlines(@Param("cutoffDate") LocalDate cutoffDate);
}