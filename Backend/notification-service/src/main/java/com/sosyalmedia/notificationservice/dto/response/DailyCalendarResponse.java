package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyCalendarResponse {
    private LocalDate date;
    private String dayOfWeek;
    private Boolean isWeekend;
    private Boolean isToday;
    private GlobalSpecialDateDTO specialDate;
    private List<MockScheduledPostDTO> posts;
    private List<CalendarEventDTO> events;
    private Map<String, List<Object>> timeline; // Saatlik timeline (00:00-23:59)
}