package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDTO {
    private Long id;
    private String eventType;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private LocalDateTime endDate;
    private Long customerId;
    private String companyName;
    private Long postId;
    private String icon;
    private String color;
    private String status;
    private Boolean hasReminder;
    private Integer reminderMinutesBefore;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
}