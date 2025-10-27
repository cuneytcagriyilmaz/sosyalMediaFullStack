package com.sosyalmedia.notificationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventRequestDTO {

    @NotBlank(message = "Event type is required")
    private String eventType; // MEETING, DEADLINE, REMINDER, CUSTOM

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;

    private LocalDateTime endDate;
    private Long customerId;
    private String icon;
    private String color;
    private Boolean hasReminder;
    private Integer reminderMinutesBefore;
    private Map<String, Object> metadata;
}