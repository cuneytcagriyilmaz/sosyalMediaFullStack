// src/main/java/com/sosyalmedia/analytics/dto/OnboardingTaskDTO.java

package com.sosyalmedia.analytics.dto;

import com.sosyalmedia.analytics.entity.OnboardingTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTaskDTO {

    private Long id;
    private Long customerId;
    private String taskName;
    private OnboardingTask.TaskType taskType;
    private OnboardingTask.Platform platform;
    private OnboardingTask.TaskStatus status;
    private Boolean connectionStatus;
    private LocalDateTime connectionDate;
    private String notes;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}