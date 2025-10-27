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
public class DayEventsDTO {
    private LocalDate date;
    private String dayOfWeek; // "Pazartesi"
    private Boolean isWeekend;
    private Boolean isToday;
    private GlobalSpecialDateDTO specialDate; // Özel gün varsa
    private List<MockScheduledPostDTO> posts; // O günün postları
    private List<CalendarEventDTO> events; // O günün manual eventleri
    private String dayStatus; // NORMAL, HAS_POSTS, HAS_SPECIAL_DATE, CRITICAL
    private int totalEvents; // Toplam event sayısı
}