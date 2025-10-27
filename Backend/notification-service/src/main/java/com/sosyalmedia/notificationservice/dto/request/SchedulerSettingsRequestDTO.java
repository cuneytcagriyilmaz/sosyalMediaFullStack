package com.sosyalmedia.notificationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerSettingsRequestDTO {

    @NotBlank(message = "Setting key is required")
    private String settingKey;

    @NotBlank(message = "Setting name is required")
    private String settingName;

    private String description;

    @NotBlank(message = "Cron expression is required")
    private String cronExpression;

    private Boolean isActive;
}