package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarStatisticsDTO {
    private int totalNormalPosts;
    private int totalSpecialDayPosts;
    private int totalManualEvents;
    private int totalSpecialDates;
    private int pendingPosts;
    private int readyPosts;
    private int publishedPosts;
    private int criticalAlerts;
}