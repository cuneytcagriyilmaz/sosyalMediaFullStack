package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.CalendarEventRequestDTO;
import com.sosyalmedia.notificationservice.model.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventService {

    /**
     * Event oluştur
     */
    CalendarEvent createEvent(CalendarEventRequestDTO requestDTO);

    /**
     * Event güncelle
     */
    CalendarEvent updateEvent(Long eventId, CalendarEventRequestDTO requestDTO);

    /**
     * Event sil
     */
    void deleteEvent(Long eventId);

    /**
     * ID'ye göre event getir
     */
    CalendarEvent getEventById(Long eventId);

    /**
     * Tarih aralığındaki eventleri getir
     */
    List<CalendarEvent> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Aya göre eventleri getir
     */
    List<CalendarEvent> getEventsByMonth(int year, int month);

    /**
     * Haftaya göre eventleri getir
     */
    List<CalendarEvent> getEventsByWeek(LocalDate weekStartDate);

    /**
     * Güne göre eventleri getir
     */
    List<CalendarEvent> getEventsByDay(LocalDate date);

    /**
     * Müşteriye göre eventleri getir
     */
    List<CalendarEvent> getCustomerEvents(Long customerId);

    /**
     * Müşteri ve tarih aralığına göre eventleri getir
     */
    List<CalendarEvent> getCustomerEventsByDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Event tipine göre getir
     */
    List<CalendarEvent> getEventsByType(String eventType);

    /**
     * Yaklaşan eventleri getir
     */
    List<CalendarEvent> getUpcomingEvents();

    /**
     * Müşterinin yaklaşan eventleri
     */
    List<CalendarEvent> getCustomerUpcomingEvents(Long customerId);

    /**
     * Bugünün eventleri
     */
    List<CalendarEvent> getTodayEvents();

    /**
     * Event durumunu güncelle
     */
    CalendarEvent updateEventStatus(Long eventId, String status);

    /**
     * Post için otomatik event oluştur
     */
    CalendarEvent createEventForPost(Long customerId, Long postId, LocalDateTime postDate);

    /**
     * Müşterinin tüm eventlerini sil
     */
    void deleteCustomerEvents(Long customerId);

    /**
     * Hatırlatıcı gereken eventleri getir
     */
    List<CalendarEvent> getEventsNeedingReminder(int minutesAhead);
}