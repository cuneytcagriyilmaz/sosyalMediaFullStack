// src/main/java/com/sosyalmedia/analytics/service/OnboardingTaskService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.OnboardingTaskDTO;
import com.sosyalmedia.analytics.entity.OnboardingTask;

import java.util.List;

public interface OnboardingTaskService {

    OnboardingTaskDTO createTask(OnboardingTaskDTO taskDTO);

    OnboardingTaskDTO updateTask(Long taskId, OnboardingTaskDTO taskDTO);

    void deleteTask(Long taskId);

    OnboardingTaskDTO getTaskById(Long taskId);

    List<OnboardingTaskDTO> getTasksByCustomerId(Long customerId);

    List<OnboardingTaskDTO> getTasksByCustomerIdAndPlatform(Long customerId, OnboardingTask.Platform platform);

    long countCompletedTasksByCustomerId(Long customerId);
}