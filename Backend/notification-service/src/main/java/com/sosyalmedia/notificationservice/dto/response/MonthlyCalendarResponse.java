package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyCalendarResponse {
    private int month;
    private int year;
    private String monthName; // "Ekim 2025"
    private int firstDayOfWeek; // 1-7 (Pazartesi-Pazar)
    private int totalDays; // 28-31
    private Map<Integer, DayEventsDTO> days; // Key: gün numarası
    private CalendarStatisticsDTO statistics;
}