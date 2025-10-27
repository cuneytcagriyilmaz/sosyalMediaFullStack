package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Okunmamış bildirimleri getir
     */
    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();

    /**
     * Okunmamış bildirimleri getir (sayfalı)
     */
    Page<Notification> findByIsReadFalse(Pageable pageable);

    /**
     * Tüm okunmamış bildirimleri getir
     */
    List<Notification> findByIsReadFalse();

    /**
     * Müşteriye göre bildirimleri getir
     */
    List<Notification> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    /**
     * Müşteriye göre bildirimleri getir (sayfalı)
     */
    Page<Notification> findByCustomerId(Long customerId, Pageable pageable);

    // ✅ EKSİK OLAN METHOD - BUNU EKLE

    /**
     * Müşterinin okunmamış bildirimlerini getir
     */
    List<Notification> findByCustomerIdAndIsReadFalse(Long customerId);

    /**
     * Bildirim tipine göre getir
     */
    List<Notification> findByNotificationType(String notificationType);

    /**
     * Severity'ye göre getir
     */
    List<Notification> findBySeverity(String severity);

    /**
     * Kritik bildirimleri getir
     */
    List<Notification> findBySeverityOrderByCreatedAtDesc(String severity);

    /**
     * Tarih aralığındaki bildirimleri getir
     */
    List<Notification> findByCreatedAtBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * E-posta durumuna göre getir
     */
    List<Notification> findByEmailStatus(String emailStatus);

    /**
     * E-posta gönderilmemiş bildirimleri getir
     */
    List<Notification> findByEmailSentFalse();

    /**
     * Post ID'ye göre bildirimleri getir
     */
    List<Notification> findByPostId(Long postId);

    /**
     * Özel gün ID'sine göre bildirimleri getir
     */
    List<Notification> findBySpecialDateId(Long specialDateId);

    /**
     * Okunmamış bildirim sayısı
     */
    long countByIsReadFalse();

    /**
     * Müşterinin okunmamış bildirim sayısı
     */
    long countByCustomerIdAndIsReadFalse(Long customerId);

    /**
     * Kritik okunmamış bildirim sayısı
     */
    long countByIsReadFalseAndSeverity(String severity);

    /**
     * Filtreleme (Karmaşık sorgu)
     */
    @Query("SELECT n FROM Notification n WHERE " +
            "(:customerId IS NULL OR n.customerId = :customerId) AND " +
            "(:notificationType IS NULL OR n.notificationType = :notificationType) AND " +
            "(:severity IS NULL OR n.severity = :severity) AND " +
            "(:isRead IS NULL OR n.isRead = :isRead) AND " +
            "(:startDate IS NULL OR n.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR n.createdAt <= :endDate) " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> findByFilters(
            @Param("customerId") Long customerId,
            @Param("notificationType") String notificationType,
            @Param("severity") String severity,
            @Param("isRead") Boolean isRead,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Son N bildirimi getir
     */
    List<Notification> findTop10ByOrderByCreatedAtDesc();

    /**
     * Müşterinin son bildirimleri
     */
    List<Notification> findTop5ByCustomerIdOrderByCreatedAtDesc(Long customerId);
}