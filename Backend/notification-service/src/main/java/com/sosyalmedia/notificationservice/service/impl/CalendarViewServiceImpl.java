package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.response.*;
import com.sosyalmedia.notificationservice.model.CalendarEvent;
import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import com.sosyalmedia.notificationservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarViewServiceImpl implements CalendarViewService {

    private final MockPostService mockPostService;
    private final CalendarEventService calendarEventService;
    private final CalendarificService calendarificService;

    private static final Map<Integer, String> TURKISH_MONTHS = Map.ofEntries(
            Map.entry(1, "Ocak"), Map.entry(2, "Şubat"), Map.entry(3, "Mart"),
            Map.entry(4, "Nisan"), Map.entry(5, "Mayıs"), Map.entry(6, "Haziran"),
            Map.entry(7, "Temmuz"), Map.entry(8, "Ağustos"), Map.entry(9, "Eylül"),
            Map.entry(10, "Ekim"), Map.entry(11, "Kasım"), Map.entry(12, "Aralık")
    );

    private static final Map<DayOfWeek, String> TURKISH_DAYS = Map.of(
            DayOfWeek.MONDAY, "Pazartesi", DayOfWeek.TUESDAY, "Salı",
            DayOfWeek.WEDNESDAY, "Çarşamba", DayOfWeek.THURSDAY, "Perşembe",
            DayOfWeek.FRIDAY, "Cuma", DayOfWeek.SATURDAY, "Cumartesi",
            DayOfWeek.SUNDAY, "Pazar"
    );

    @Override
    public MonthlyCalendarResponse getMonthlyCalendar(int year, int month) {
        log.info("📅 Aylık takvim hazırlanıyor: {} {}", year, month);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        // Tüm postları getir
        List<MockScheduledPost> posts = mockPostService.getPostsByDateRange(
                firstDay.atStartOfDay(),
                lastDay.atTime(23, 59)
        );

        // Tüm eventleri getir
        List<CalendarEvent> events = calendarEventService.getEventsByMonth(year, month);

        // Özel günleri getir
        List<GlobalSpecialDate> specialDates = calendarificService.getHolidaysByYear(year);

        return buildMonthlyCalendar(year, month, posts, events, specialDates, null);
    }

    @Override
    public MonthlyCalendarResponse getCustomerMonthlyCalendar(Long customerId, int year, int month) {
        log.info("📅 Müşteri aylık takvimi hazırlanıyor: Customer ID: {}, {} {}", customerId, year, month);

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        // Müşterinin postları
        List<MockScheduledPost> posts = mockPostService.getCustomerPostsByDateRange(
                customerId,
                firstDay.atStartOfDay(),
                lastDay.atTime(23, 59)
        );

        // Müşterinin eventleri
        List<CalendarEvent> events = calendarEventService.getCustomerEventsByDateRange(
                customerId,
                firstDay.atStartOfDay(),
                lastDay.atTime(23, 59)
        );

        // Özel günler (tüm müşteriler için aynı)
        List<GlobalSpecialDate> specialDates = calendarificService.getHolidaysByYear(year);

        return buildMonthlyCalendar(year, month, posts, events, specialDates, customerId);
    }

    @Override
    public WeeklyCalendarResponse getWeeklyCalendar(LocalDate weekStartDate) {
        log.info("📅 Haftalık takvim hazırlanıyor: {}", weekStartDate);

        LocalDate weekEndDate = weekStartDate.plusDays(6);

        // Tüm postları getir
        List<MockScheduledPost> posts = mockPostService.getPostsByDateRange(
                weekStartDate.atStartOfDay(),
                weekEndDate.atTime(23, 59)
        );

        // Tüm eventleri getir
        List<CalendarEvent> events = calendarEventService.getEventsByDateRange(
                weekStartDate.atStartOfDay(),
                weekEndDate.atTime(23, 59)
        );

        // Özel günleri getir
        List<GlobalSpecialDate> specialDates = calendarificService.getHolidaysByYear(weekStartDate.getYear());

        return buildWeeklyCalendar(weekStartDate, posts, events, specialDates, null);
    }

    @Override
    public WeeklyCalendarResponse getCustomerWeeklyCalendar(Long customerId, LocalDate weekStartDate) {
        log.info("📅 Müşteri haftalık takvimi hazırlanıyor: Customer ID: {}, {}", customerId, weekStartDate);

        LocalDate weekEndDate = weekStartDate.plusDays(6);

        // Müşterinin postları
        List<MockScheduledPost> posts = mockPostService.getCustomerPostsByDateRange(
                customerId,
                weekStartDate.atStartOfDay(),
                weekEndDate.atTime(23, 59)
        );

        // Müşterinin eventleri
        List<CalendarEvent> events = calendarEventService.getCustomerEventsByDateRange(
                customerId,
                weekStartDate.atStartOfDay(),
                weekEndDate.atTime(23, 59)
        );

        // Özel günler
        List<GlobalSpecialDate> specialDates = calendarificService.getHolidaysByYear(weekStartDate.getYear());

        return buildWeeklyCalendar(weekStartDate, posts, events, specialDates, customerId);
    }

    @Override
    public DailyCalendarResponse getDailyCalendar(LocalDate date) {
        log.info("📅 Günlük takvim hazırlanıyor: {}", date);

        // Tüm postları getir
        List<MockScheduledPost> posts = mockPostService.getPostsByDateRange(
                date.atStartOfDay(),
                date.atTime(23, 59)
        );

        // Tüm eventleri getir
        List<CalendarEvent> events = calendarEventService.getEventsByDay(date);

        // Özel gün var mı?
        List<GlobalSpecialDate> specialDates = calendarificService.getHolidaysByYear(date.getYear());
        GlobalSpecialDate specialDate = specialDates.stream()
                .filter(sd -> sd.getDateValue().equals(date))
                .findFirst()
                .orElse(null);

        return buildDailyCalendar(date, posts, events, specialDate, null);
    }

    @Override
    public DailyCalendarResponse getCustomerDailyCalendar(Long customerId, LocalDate date) {
        log.info("📅 Müşteri günlük takvimi hazırlanıyor: Customer ID: {}, {}", customerId, date);

        // Müşterinin postları
        List<MockScheduledPost> posts = mockPostService.getCustomerPostsByDateRange(
                customerId,
                date.atStartOfDay(),
                date.atTime(23, 59)
        );

        // Müşterinin eventleri
        List<CalendarEvent> events = calendarEventService.getCustomerEventsByDateRange(
                customerId,
                date.atStartOfDay(),
                date.atTime(23, 59)
        );

        // Özel gün var mı?
        List<GlobalSpecialDate> specialDates = calendarificService.getHolidaysByYear(date.getYear());
        GlobalSpecialDate specialDate = specialDates.stream()
                .filter(sd -> sd.getDateValue().equals(date))
                .findFirst()
                .orElse(null);

        return buildDailyCalendar(date, posts, events, specialDate, customerId);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Aylık takvim oluştur
     */
    private MonthlyCalendarResponse buildMonthlyCalendar(
            int year,
            int month,
            List<MockScheduledPost> posts,
            List<CalendarEvent> events,
            List<GlobalSpecialDate> specialDates,
            Long customerId
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        LocalDate firstDay = yearMonth.atDay(1);

        Map<Integer, DayEventsDTO> daysMap = new HashMap<>();

        // Her gün için event bilgilerini topla
        for (int day = 1; day <= totalDays; day++) {
            LocalDate date = LocalDate.of(year, month, day);

            List<MockScheduledPost> dayPosts = filterPostsByDate(posts, date);
            List<CalendarEvent> dayEvents = filterEventsByDate(events, date);
            GlobalSpecialDate daySpecialDate = findSpecialDateByDate(specialDates, date);

            DayEventsDTO dayEventsDTO = buildDayEventsDTO(date, dayPosts, dayEvents, daySpecialDate);
            daysMap.put(day, dayEventsDTO);
        }

        // İstatistikleri hesapla
        CalendarStatisticsDTO statistics = calculateStatistics(posts, events, specialDates);

        return MonthlyCalendarResponse.builder()
                .month(month)
                .year(year)
                .monthName(TURKISH_MONTHS.get(month) + " " + year)
                .firstDayOfWeek(firstDay.getDayOfWeek().getValue())
                .totalDays(totalDays)
                .days(daysMap)
                .statistics(statistics)
                .build();
    }

    /**
     * Haftalık takvim oluştur
     */
    private WeeklyCalendarResponse buildWeeklyCalendar(
            LocalDate weekStartDate,
            List<MockScheduledPost> posts,
            List<CalendarEvent> events,
            List<GlobalSpecialDate> specialDates,
            Long customerId
    ) {
        LocalDate weekEndDate = weekStartDate.plusDays(6);
        int weekNumber = weekStartDate.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());

        List<DayEventsDTO> days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStartDate.plusDays(i);

            List<MockScheduledPost> dayPosts = filterPostsByDate(posts, date);
            List<CalendarEvent> dayEvents = filterEventsByDate(events, date);
            GlobalSpecialDate daySpecialDate = findSpecialDateByDate(specialDates, date);

            DayEventsDTO dayEventsDTO = buildDayEventsDTO(date, dayPosts, dayEvents, daySpecialDate);
            days.add(dayEventsDTO);
        }

        CalendarStatisticsDTO statistics = calculateStatistics(posts, events, specialDates);

        return WeeklyCalendarResponse.builder()
                .weekStartDate(weekStartDate)
                .weekEndDate(weekEndDate)
                .weekNumber(weekNumber)
                .year(weekStartDate.getYear())
                .days(days)
                .statistics(statistics)
                .build();
    }

    /**
     * Günlük takvim oluştur
     */
    private DailyCalendarResponse buildDailyCalendar(
            LocalDate date,
            List<MockScheduledPost> posts,
            List<CalendarEvent> events,
            GlobalSpecialDate specialDate,
            Long customerId
    ) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        boolean isToday = date.equals(LocalDate.now());

        // Saatlik timeline oluştur (00:00 - 23:59)
        Map<String, List<Object>> timeline = buildHourlyTimeline(posts, events);

        return DailyCalendarResponse.builder()
                .date(date)
                .dayOfWeek(TURKISH_DAYS.get(dayOfWeek))
                .isWeekend(isWeekend)
                .isToday(isToday)
                .specialDate(specialDate != null ? convertToSpecialDateDTO(specialDate) : null)
                .posts(convertToPostDTOs(posts))
                .events(convertToEventDTOs(events))
                .timeline(timeline)
                .build();
    }

    /**
     * Gün event DTO'su oluştur
     */
    private DayEventsDTO buildDayEventsDTO(
            LocalDate date,
            List<MockScheduledPost> posts,
            List<CalendarEvent> events,
            GlobalSpecialDate specialDate
    ) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        boolean isToday = date.equals(LocalDate.now());

        String dayStatus = determineDayStatus(posts, events, specialDate);
        int totalEvents = posts.size() + events.size() + (specialDate != null ? 1 : 0);

        return DayEventsDTO.builder()
                .date(date)
                .dayOfWeek(TURKISH_DAYS.get(dayOfWeek))
                .isWeekend(isWeekend)
                .isToday(isToday)
                .specialDate(specialDate != null ? convertToSpecialDateDTO(specialDate) : null)
                .posts(convertToPostDTOs(posts))
                .events(convertToEventDTOs(events))
                .dayStatus(dayStatus)
                .totalEvents(totalEvents)
                .build();
    }

    /**
     * Tarihe göre postları filtrele
     */
    private List<MockScheduledPost> filterPostsByDate(List<MockScheduledPost> posts, LocalDate date) {
        return posts.stream()
                .filter(post -> post.getScheduledDate().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Tarihe göre eventleri filtrele
     */
    private List<CalendarEvent> filterEventsByDate(List<CalendarEvent> events, LocalDate date) {
        return events.stream()
                .filter(event -> event.getEventDate().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Tarihe göre özel gün bul
     */
    private GlobalSpecialDate findSpecialDateByDate(List<GlobalSpecialDate> specialDates, LocalDate date) {
        return specialDates.stream()
                .filter(sd -> sd.getDateValue().equals(date))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gün durumunu belirle
     */
    private String determineDayStatus(
            List<MockScheduledPost> posts,
            List<CalendarEvent> events,
            GlobalSpecialDate specialDate
    ) {
        if (specialDate != null) {
            return "HAS_SPECIAL_DATE";
        }

        long criticalPosts = posts.stream()
                .filter(post -> "NOT_STARTED".equals(post.getPreparationStatus())
                        || "IN_PROGRESS".equals(post.getPreparationStatus()))
                .count();

        if (criticalPosts > 0) {
            return "CRITICAL";
        }

        if (!posts.isEmpty()) {
            return "HAS_POSTS";
        }

        return "NORMAL";
    }

    /**
     * İstatistikleri hesapla
     */
    private CalendarStatisticsDTO calculateStatistics(
            List<MockScheduledPost> posts,
            List<CalendarEvent> events,
            List<GlobalSpecialDate> specialDates
    ) {
        int totalNormalPosts = (int) posts.stream().filter(p -> !p.getIsSpecialDayPost()).count();
        int totalSpecialDayPosts = (int) posts.stream().filter(MockScheduledPost::getIsSpecialDayPost).count();
        int totalManualEvents = events.size();
        int totalSpecialDates = specialDates.size();
        int pendingPosts = (int) posts.stream().filter(p -> "SCHEDULED".equals(p.getStatus())).count();
        int readyPosts = (int) posts.stream().filter(p -> "READY".equals(p.getPreparationStatus())).count();
        int publishedPosts = (int) posts.stream().filter(p -> "PUBLISHED".equals(p.getStatus())).count();
        int criticalAlerts = (int) posts.stream()
                .filter(p -> "NOT_STARTED".equals(p.getPreparationStatus()) && "SCHEDULED".equals(p.getStatus()))
                .count();

        return CalendarStatisticsDTO.builder()
                .totalNormalPosts(totalNormalPosts)
                .totalSpecialDayPosts(totalSpecialDayPosts)
                .totalManualEvents(totalManualEvents)
                .totalSpecialDates(totalSpecialDates)
                .pendingPosts(pendingPosts)
                .readyPosts(readyPosts)
                .publishedPosts(publishedPosts)
                .criticalAlerts(criticalAlerts)
                .build();
    }

    /**
     * Saatlik timeline oluştur
     */
    private Map<String, List<Object>> buildHourlyTimeline(
            List<MockScheduledPost> posts,
            List<CalendarEvent> events
    ) {
        Map<String, List<Object>> timeline = new TreeMap<>();

        // 00:00 - 23:00 arası tüm saatleri oluştur
        for (int hour = 0; hour < 24; hour++) {
            String hourKey = String.format("%02d:00", hour);
            timeline.put(hourKey, new ArrayList<>());
        }

        // Postları timeline'a ekle
        for (MockScheduledPost post : posts) {
            int hour = post.getScheduledDate().getHour();
            String hourKey = String.format("%02d:00", hour);
            timeline.get(hourKey).add(post);
        }

        // Eventleri timeline'a ekle
        for (CalendarEvent event : events) {
            int hour = event.getEventDate().getHour();
            String hourKey = String.format("%02d:00", hour);
            timeline.get(hourKey).add(event);
        }

        return timeline;
    }

    // Converter methods
    private List<MockScheduledPostDTO> convertToPostDTOs(List<MockScheduledPost> posts) {
        return posts.stream().map(this::convertToPostDTO).collect(Collectors.toList());
    }

    private MockScheduledPostDTO convertToPostDTO(MockScheduledPost post) {
        return MockScheduledPostDTO.builder()
                .id(post.getId())
                .customerId(post.getCustomerId())
                .scheduledDate(post.getScheduledDate())
                .postType(post.getPostType())
                .isSpecialDayPost(post.getIsSpecialDayPost())
                .specialDateId(post.getSpecialDateId())
                .status(post.getStatus())
                .preparationStatus(post.getPreparationStatus())
                .content(post.getContent())
                .platforms(post.getPlatforms())
                .build();
    }

    private List<CalendarEventDTO> convertToEventDTOs(List<CalendarEvent> events) {
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    private CalendarEventDTO convertToEventDTO(CalendarEvent event) {
        return CalendarEventDTO.builder()
                .id(event.getId())
                .eventType(event.getEventType())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .endDate(event.getEndDate())
                .customerId(event.getCustomerId())
                .postId(event.getPostId())
                .icon(event.getIcon())
                .color(event.getColor())
                .status(event.getStatus())
                .hasReminder(event.getHasReminder())
                .reminderMinutesBefore(event.getReminderMinutesBefore())
                .metadata(event.getMetadata())
                .createdAt(event.getCreatedAt())
                .build();
    }

    private GlobalSpecialDateDTO convertToSpecialDateDTO(GlobalSpecialDate specialDate) {
        return GlobalSpecialDateDTO.builder()
                .id(specialDate.getId())
                .dateType(specialDate.getDateType())
                .dateName(specialDate.getDateName())
                .dateValue(specialDate.getDateValue())
                .description(specialDate.getDescription())
                .icon(specialDate.getIcon())
                .isRecurring(specialDate.getIsRecurring())
                .autoSuggestPost(specialDate.getAutoSuggestPost())
                .build();
    }
}