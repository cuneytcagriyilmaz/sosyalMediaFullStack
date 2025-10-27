package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.response.DailyCalendarResponse;
import com.sosyalmedia.notificationservice.dto.response.MonthlyCalendarResponse;
import com.sosyalmedia.notificationservice.dto.response.WeeklyCalendarResponse;
import com.sosyalmedia.notificationservice.service.CalendarViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final CalendarViewService calendarViewService;

    /**
     * Aylık takvim görünümü
     * GET /api/calendar/monthly?year=2025&month=1
     */
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month
    ) {
        log.info("📅 Aylık takvim istendi: {} / {}", year, month);
        MonthlyCalendarResponse calendar = calendarViewService.getMonthlyCalendar(year, month);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Müşteriye özel aylık takvim
     * GET /api/calendar/monthly/customer/{customerId}?year=2025&month=1
     */
    @GetMapping("/monthly/customer/{customerId}")
    public ResponseEntity<MonthlyCalendarResponse> getCustomerMonthlyCalendar(
            @PathVariable Long customerId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        log.info("📅 Müşteri aylık takvimi istendi - Customer ID: {}, {} / {}", customerId, year, month);
        MonthlyCalendarResponse calendar = calendarViewService.getCustomerMonthlyCalendar(customerId, year, month);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Haftalık takvim görünümü
     * GET /api/calendar/weekly?startDate=2025-01-27
     */
    @GetMapping("/weekly")
    public ResponseEntity<WeeklyCalendarResponse> getWeeklyCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        log.info("📅 Haftalık takvim istendi: {}", startDate);
        WeeklyCalendarResponse calendar = calendarViewService.getWeeklyCalendar(startDate);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Müşteriye özel haftalık takvim
     * GET /api/calendar/weekly/customer/{customerId}?startDate=2025-01-27
     */
    @GetMapping("/weekly/customer/{customerId}")
    public ResponseEntity<WeeklyCalendarResponse> getCustomerWeeklyCalendar(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        log.info("📅 Müşteri haftalık takvimi - Customer ID: {}, {}", customerId, startDate);
        WeeklyCalendarResponse calendar = calendarViewService.getCustomerWeeklyCalendar(customerId, startDate);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Günlük takvim görünümü
     * GET /api/calendar/daily?date=2025-01-27
     */
    @GetMapping("/daily")
    public ResponseEntity<DailyCalendarResponse> getDailyCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("📅 Günlük takvim istendi: {}", date);
        DailyCalendarResponse calendar = calendarViewService.getDailyCalendar(date);
        return ResponseEntity.ok(calendar);
    }

    /**
     * Müşteriye özel günlük takvim
     * GET /api/calendar/daily/customer/{customerId}?date=2025-01-27
     */
    @GetMapping("/daily/customer/{customerId}")
    public ResponseEntity<DailyCalendarResponse> getCustomerDailyCalendar(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("📅 Müşteri günlük takvimi - Customer ID: {}, {}", customerId, date);
        DailyCalendarResponse calendar = calendarViewService.getCustomerDailyCalendar(customerId, date);
        return ResponseEntity.ok(calendar);
    }
}