package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerSettingsDTO {
    private Long id;
    private String settingKey;
    private String settingName;
    private String description;
    private String cronExpression;
    private Boolean isActive;
    private LocalDateTime lastExecutedAt;
    private LocalDateTime nextExecutionAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}