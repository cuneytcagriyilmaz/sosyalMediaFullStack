package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    @Query("SELECT pd FROM PostDeadline pd WHERE pd.scheduledDate < :today AND pd.status != 'SENT' ORDER BY pd.scheduledDate ASC")
    List<PostDeadline> findOverdueDeadlines(@Param("today") LocalDate today);

    long countByStatus(PostDeadlineStatus status);

    @Query("SELECT COUNT(pd) FROM PostDeadline pd WHERE pd.scheduledDate BETWEEN :today AND :endDate")
    long countUpcomingWeek(@Param("today") LocalDate today, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(pd) FROM PostDeadline pd WHERE pd.scheduledDate < :today AND pd.status != 'SENT'")
    long countOverdue(@Param("today") LocalDate today);

    //  DÜZELTME: PostgreSQL için DATEDIFF yerine CAST kullan
    @Query("SELECT AVG(CAST(pd.scheduledDate - CURRENT_DATE AS double)) FROM PostDeadline pd WHERE pd.scheduledDate >= CURRENT_DATE")
    Double getAverageDaysRemaining();
}