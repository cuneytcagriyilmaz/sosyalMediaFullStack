package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.response.DailyCalendarResponse;
import com.sosyalmedia.notificationservice.dto.response.MonthlyCalendarResponse;
import com.sosyalmedia.notificationservice.dto.response.WeeklyCalendarResponse;

import java.time.LocalDate;

public interface CalendarViewService {

    /**
     * Aylık takvim görünümü
     */
    MonthlyCalendarResponse getMonthlyCalendar(int year, int month);

    /**
     * Aylık takvim görünümü (müşteriye özel)
     */
    MonthlyCalendarResponse getCustomerMonthlyCalendar(Long customerId, int year, int month);

    /**
     * Haftalık takvim görünümü
     */
    WeeklyCalendarResponse getWeeklyCalendar(LocalDate weekStartDate);

    /**
     * Haftalık takvim görünümü (müşteriye özel)
     */
    WeeklyCalendarResponse getCustomerWeeklyCalendar(Long customerId, LocalDate weekStartDate);

    /**
     * Günlük takvim görünümü
     */
    DailyCalendarResponse getDailyCalendar(LocalDate date);

    /**
     * Günlük takvim görünümü (müşteriye özel)
     */
    DailyCalendarResponse getCustomerDailyCalendar(Long customerId, LocalDate date);
}