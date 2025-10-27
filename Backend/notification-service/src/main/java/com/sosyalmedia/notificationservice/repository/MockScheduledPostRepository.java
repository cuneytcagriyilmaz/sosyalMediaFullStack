package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MockScheduledPostRepository extends JpaRepository<MockScheduledPost, Long> {

    /**
     * Müşteriye göre postları getir (liste)
     */
    List<MockScheduledPost> findByCustomerIdOrderByScheduledDateAsc(Long customerId);

    /**
     * Müşteriye göre postları getir (sayfalı)
     */
    Page<MockScheduledPost> findByCustomerId(Long customerId, Pageable pageable); // ✅ EKLE

    /**
     * Tarih aralığındaki postları getir
     */
    List<MockScheduledPost> findByScheduledDateBetweenOrderByScheduledDateAsc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Müşteri ve tarih aralığına göre postları getir
     */
    List<MockScheduledPost> findByCustomerIdAndScheduledDateBetween(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Duruma göre postları getir
     */
    List<MockScheduledPost> findByStatus(String status);

    /**
     * Hazırlık durumuna göre postları getir
     */
    List<MockScheduledPost> findByPreparationStatus(String preparationStatus);

    /**
     * Yaklaşan postları getir
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE p.scheduledDate >= :startDate " +
            "AND p.scheduledDate <= :endDate AND p.status != 'PUBLISHED' " +
            "ORDER BY p.scheduledDate ASC")
    List<MockScheduledPost> findUpcomingPosts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Müşterinin yaklaşan postları
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE p.customerId = :customerId " +
            "AND p.scheduledDate >= :startDate AND p.scheduledDate <= :endDate " +
            "AND p.status != 'PUBLISHED' ORDER BY p.scheduledDate ASC")
    List<MockScheduledPost> findUpcomingPostsByCustomer(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Bugünün postları
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE " +
            "FUNCTION('DATE', p.scheduledDate) = :date ORDER BY p.scheduledDate ASC")
    List<MockScheduledPost> findTodayPosts(@Param("date") LocalDate date);

    /**
     * Müşterinin bugünkü postları
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE p.customerId = :customerId " +
            "AND FUNCTION('DATE', p.scheduledDate) = :date ORDER BY p.scheduledDate ASC")
    List<MockScheduledPost> findTodayPostsByCustomer(
            @Param("customerId") Long customerId,
            @Param("date") LocalDate date
    );

    /**
     * Gecikmiş postları getir
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE p.scheduledDate < :currentDate " +
            "AND p.status != 'PUBLISHED' ORDER BY p.scheduledDate DESC")
    List<MockScheduledPost> findOverduePosts(@Param("currentDate") LocalDateTime currentDate);

    /**
     * Müşterinin gecikmiş postları
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE p.customerId = :customerId " +
            "AND p.scheduledDate < :currentDate AND p.status != 'PUBLISHED' " +
            "ORDER BY p.scheduledDate DESC")
    List<MockScheduledPost> findOverduePostsByCustomer(
            @Param("customerId") Long customerId,
            @Param("currentDate") LocalDateTime currentDate
    );

    /**
     * Kritik postları getir (yaklaşan ve hazır olmayan)
     */
    @Query("SELECT p FROM MockScheduledPost p WHERE p.scheduledDate >= :startDate " +
            "AND p.scheduledDate <= :endDate AND p.preparationStatus != 'READY' " +
            "AND p.status != 'PUBLISHED' ORDER BY p.scheduledDate ASC")
    List<MockScheduledPost> findCriticalPosts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Özel gün postlarını getir
     */
    List<MockScheduledPost> findByIsSpecialDayPostTrue();

    /**
     * Normal postları getir
     */
    List<MockScheduledPost> findByIsSpecialDayPostFalse();

    /**
     * Müşterinin tüm postlarını sil
     */
    void deleteByCustomerId(Long customerId);
}