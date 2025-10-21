// src/main/java/com/sosyalmedia/analytics/dto/AIContentTaskDTO.java

package com.sosyalmedia.analytics.dto;

import com.sosyalmedia.analytics.entity.AIContentTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIContentTaskDTO {

    private Long id;
    private Long customerId;
    private String taskName;
    private AIContentTask.TaskType taskType;
    private AIContentTask.TaskStatus status;
    private Integer quantity;
    private Integer progressCurrent;
    private Integer progressTotal;
    private String notes;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}