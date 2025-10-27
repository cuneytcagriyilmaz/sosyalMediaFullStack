package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyCalendarResponse {
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private int weekNumber;
    private int year;
    private List<DayEventsDTO> days; // 7 gün
    private CalendarStatisticsDTO statistics;
}