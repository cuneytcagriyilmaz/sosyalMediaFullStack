package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.request.CalendarEventRequestDTO;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.CalendarEvent;
import com.sosyalmedia.notificationservice.repository.CalendarEventRepository;
import com.sosyalmedia.notificationservice.service.CalendarEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository eventRepository;

    @Override
    @Transactional
    public CalendarEvent createEvent(CalendarEventRequestDTO requestDTO) {
        log.info("📅 Yeni calendar event oluşturuluyor: {}", requestDTO.getTitle());

        CalendarEvent event = CalendarEvent.builder()
                .eventType(requestDTO.getEventType())
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .eventDate(requestDTO.getEventDate())
                .endDate(requestDTO.getEndDate())
                .customerId(requestDTO.getCustomerId())
                .icon(requestDTO.getIcon() != null ? requestDTO.getIcon() : getDefaultIcon(requestDTO.getEventType()))
                .color(requestDTO.getColor() != null ? requestDTO.getColor() : getDefaultColor(requestDTO.getEventType()))
                .status("PENDING")
                .hasReminder(requestDTO.getHasReminder() != null ? requestDTO.getHasReminder() : false)
                .reminderMinutesBefore(requestDTO.getReminderMinutesBefore())
                .metadata(requestDTO.getMetadata())
                .createdBy("SYSTEM")
                .build();

        CalendarEvent saved = eventRepository.save(event);

        log.info("✅ Calendar event oluşturuldu - ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public CalendarEvent updateEvent(Long eventId, CalendarEventRequestDTO requestDTO) {
        CalendarEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent", "id", eventId));

        log.info("📝 Calendar event güncelleniyor - ID: {}", eventId);

        event.setEventType(requestDTO.getEventType());
        event.setTitle(requestDTO.getTitle());
        event.setDescription(requestDTO.getDescription());
        event.setEventDate(requestDTO.getEventDate());
        event.setEndDate(requestDTO.getEndDate());
        event.setCustomerId(requestDTO.getCustomerId());
        event.setIcon(requestDTO.getIcon());
        event.setColor(requestDTO.getColor());
        event.setHasReminder(requestDTO.getHasReminder());
        event.setReminderMinutesBefore(requestDTO.getReminderMinutesBefore());
        event.setMetadata(requestDTO.getMetadata());

        CalendarEvent updated = eventRepository.save(event);

        log.info("✅ Calendar event güncellendi - ID: {}", eventId);
        return updated;
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("CalendarEvent", "id", eventId);
        }

        eventRepository.deleteById(eventId);
        log.info("🗑️ Calendar event silindi - ID: {}", eventId);
    }

    @Override
    public CalendarEvent getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent", "id", eventId));
    }

    @Override
    public List<CalendarEvent> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByEventDateBetweenOrderByEventDateAsc(startDate, endDate);
    }

    @Override
    public List<CalendarEvent> getEventsByMonth(int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);

        return getEventsByDateRange(startDate, endDate);
    }

    @Override
    public List<CalendarEvent> getEventsByWeek(LocalDate weekStartDate) {
        LocalDateTime startDate = weekStartDate.atStartOfDay();
        LocalDateTime endDate = weekStartDate.plusDays(6).atTime(23, 59);

        return getEventsByDateRange(startDate, endDate);
    }

    @Override
    public List<CalendarEvent> getEventsByDay(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59);

        return getEventsByDateRange(startDate, endDate);
    }

    @Override
    public List<CalendarEvent> getCustomerEvents(Long customerId) {
        return eventRepository.findByCustomerId(customerId);
    }

    @Override
    public List<CalendarEvent> getCustomerEventsByDateRange(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return eventRepository.findByCustomerIdAndEventDateBetween(customerId, startDate, endDate);
    }

    @Override
    public List<CalendarEvent> getEventsByType(String eventType) {
        return eventRepository.findByEventType(eventType);
    }

    @Override
    public List<CalendarEvent> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents(LocalDateTime.now());
    }

    @Override
    public List<CalendarEvent> getCustomerUpcomingEvents(Long customerId) {
        return eventRepository.findUpcomingEventsByCustomer(customerId, LocalDateTime.now());
    }

    @Override
    public List<CalendarEvent> getTodayEvents() {
        return eventRepository.findTodayEvents();
    }

    @Override
    @Transactional
    public CalendarEvent updateEventStatus(Long eventId, String status) {
        CalendarEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent", "id", eventId));

        event.setStatus(status);

        CalendarEvent updated = eventRepository.save(event);

        log.info("🔄 Event durumu güncellendi - ID: {}, Durum: {}", eventId, status);
        return updated;
    }

    @Override
    @Transactional
    public CalendarEvent createEventForPost(Long customerId, Long postId, LocalDateTime postDate) {
        log.info("📅 Post için otomatik event oluşturuluyor - Post ID: {}", postId);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("autoCreated", true);
        metadata.put("postId", postId);

        CalendarEvent event = CalendarEvent.builder()
                .eventType("POST_SCHEDULED")
                .title("Post Yayınlanacak")
                .description("Planlı post yayınlanacak")
                .eventDate(postDate)
                .customerId(customerId)
                .postId(postId)
                .icon("📝")
                .color("primary")
                .status("PENDING")
                .hasReminder(true)
                .reminderMinutesBefore(60) // 1 saat önce hatırlat
                .metadata(metadata)
                .createdBy("SYSTEM")
                .build();

        CalendarEvent saved = eventRepository.save(event);

        log.info("✅ Post event oluşturuldu - Event ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public void deleteCustomerEvents(Long customerId) {
        eventRepository.deleteByCustomerId(customerId);
        log.info("🗑️ {} numaralı müşterinin tüm eventleri silindi", customerId);
    }

    @Override
    public List<CalendarEvent> getEventsNeedingReminder(int minutesAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusMinutes(minutesAhead);

        // ✅ DÜZELTİLDİ: start ve end parametreleri
        return eventRepository.findEventsNeedingReminder(now, endDate);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Event tipine göre default icon belirle
     */
    private String getDefaultIcon(String eventType) {
        return switch (eventType) {
            case "POST_SCHEDULED" -> "📝";
            case "CUSTOMER_REGISTERED" -> "👤";
            case "MEETING" -> "👥";
            case "DEADLINE" -> "⏰";
            case "REMINDER" -> "🔔";
            case "CUSTOM" -> "📌";
            default -> "📅";
        };
    }

    /**
     * Event tipine göre default color belirle
     */
    private String getDefaultColor(String eventType) {
        return switch (eventType) {
            case "POST_SCHEDULED" -> "primary";
            case "CUSTOMER_REGISTERED" -> "success";
            case "MEETING" -> "info";
            case "DEADLINE" -> "danger";
            case "REMINDER" -> "warning";
            case "CUSTOM" -> "secondary";
            default -> "primary";
        };
    }
}