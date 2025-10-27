package com.sosyalmedia.notificationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerUpdateDTO {

    @NotBlank(message = "Cron expression is required")
    @Pattern(regexp = "^[0-9*/,\\-\\s]+$", message = "Invalid cron expression")
    private String cronExpression;

    private Boolean isActive;
}