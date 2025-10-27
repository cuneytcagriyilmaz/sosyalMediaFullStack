package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    /**
     * Tarih aralığındaki eventleri getir
     */
    List<CalendarEvent> findByEventDateBetweenOrderByEventDateAsc(
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Müşteriye göre eventleri getir
     */
    List<CalendarEvent> findByCustomerId(Long customerId);

    /**
     * Müşteri ve tarih aralığına göre getir
     */
    List<CalendarEvent> findByCustomerIdAndEventDateBetween(
            Long customerId,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Event tipine göre getir
     */
    List<CalendarEvent> findByEventType(String eventType);

    /**
     * Event tipi ve tarih aralığına göre getir
     */
    List<CalendarEvent> findByEventTypeAndEventDateBetween(
            String eventType,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Duruma göre getir
     */
    List<CalendarEvent> findByStatus(String status);

    /**
     * Durum ve tarihten önceki eventler
     */
    List<CalendarEvent> findByStatusAndEventDateBefore(
            String status,
            LocalDateTime date
    );

    /**
     * Hatırlatıcı gereken eventleri getir
     */
    @Query("SELECT e FROM CalendarEvent e WHERE e.hasReminder = true " +
            "AND e.status = 'PENDING' " +
            "AND e.eventDate BETWEEN :start AND :end")
    List<CalendarEvent> findEventsNeedingReminder(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    /**
     * Yaklaşan eventleri getir
     */
    @Query("SELECT e FROM CalendarEvent e WHERE e.eventDate >= :today " +
            "AND e.status != 'CANCELLED' ORDER BY e.eventDate")
    List<CalendarEvent> findUpcomingEvents(@Param("today") LocalDateTime today);

    /**
     * Müşterinin yaklaşan eventleri
     */
    @Query("SELECT e FROM CalendarEvent e WHERE e.customerId = :customerId " +
            "AND e.eventDate >= :today AND e.status != 'CANCELLED' " +
            "ORDER BY e.eventDate")
    List<CalendarEvent> findUpcomingEventsByCustomer(
            @Param("customerId") Long customerId,
            @Param("today") LocalDateTime today
    );

    /**
     * Bugünün eventleri
     */
    @Query("SELECT e FROM CalendarEvent e WHERE " +
            "FUNCTION('DATE', e.eventDate) = CURRENT_DATE " +
            "AND e.status != 'CANCELLED' ORDER BY e.eventDate")
    List<CalendarEvent> findTodayEvents();

    /**
     * Post ID'ye göre eventleri getir
     */
    List<CalendarEvent> findByPostId(Long postId);

    /**
     * Müşterinin tüm eventlerini sil
     */
    void deleteByCustomerId(Long customerId);
}