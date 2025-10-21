// src/main/java/com/sosyalmedia/analytics/service/AIContentTaskService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.AIContentTaskDTO;
import com.sosyalmedia.analytics.entity.AIContentTask;

import java.util.List;

public interface AIContentTaskService {

    AIContentTaskDTO createTask(AIContentTaskDTO taskDTO);

    AIContentTaskDTO updateTask(Long taskId, AIContentTaskDTO taskDTO);

    void deleteTask(Long taskId);

    AIContentTaskDTO getTaskById(Long taskId);

    List<AIContentTaskDTO> getTasksByCustomerId(Long customerId);

    List<AIContentTaskDTO> getTasksByStatus(AIContentTask.TaskStatus status);

    long countTasksByCustomerId(Long customerId);
}