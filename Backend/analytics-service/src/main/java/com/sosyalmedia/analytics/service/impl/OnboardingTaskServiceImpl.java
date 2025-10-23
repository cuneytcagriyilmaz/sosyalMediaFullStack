// src/main/java/com/sosyalmedia/analytics/service/impl/OnboardingTaskServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.dto.OnboardingTaskDTO;
import com.sosyalmedia.analytics.entity.OnboardingTask;
import com.sosyalmedia.analytics.exception.ResourceNotFoundException;
import com.sosyalmedia.analytics.mapper.OnboardingTaskMapper;
import com.sosyalmedia.analytics.repository.OnboardingTaskRepository;
import com.sosyalmedia.analytics.service.OnboardingTaskService;
import com.sosyalmedia.analytics.service.CustomerValidationService; // ✅ YENİ
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OnboardingTaskServiceImpl implements OnboardingTaskService {

    private final OnboardingTaskRepository taskRepository;
    private final OnboardingTaskMapper taskMapper;
    private final CustomerValidationService customerValidationService; // ✅ YENİ

    @Override
    public OnboardingTaskDTO createTask(OnboardingTaskDTO taskDTO) {
        log.info("Creating new Onboarding Task for customer: {}", taskDTO.getCustomerId());

        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(taskDTO.getCustomerId());

        OnboardingTask task = taskMapper.toEntity(taskDTO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        OnboardingTask savedTask = taskRepository.save(task);
        log.info("Onboarding Task created successfully with ID: {}", savedTask.getId());

        return taskMapper.toDTO(savedTask);
    }

    @Override
    public OnboardingTaskDTO updateTask(Long taskId, OnboardingTaskDTO taskDTO) {
        log.info("Updating Onboarding Task with ID: {}", taskId);

        OnboardingTask existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("OnboardingTask", "id", taskId));

        // ✅ VALIDATION: Eğer customerId değişiyorsa, yeni customer'ı kontrol et
        if (taskDTO.getCustomerId() != null &&
                !taskDTO.getCustomerId().equals(existingTask.getCustomerId())) {
            customerValidationService.validateCustomerExists(taskDTO.getCustomerId());
        }

        // Mapper ile güncelleme
        taskMapper.updateEntityFromDTO(taskDTO, existingTask);
        existingTask.setUpdatedAt(LocalDateTime.now());

        OnboardingTask updatedTask = taskRepository.save(existingTask);
        log.info("Onboarding Task updated successfully with ID: {}", updatedTask.getId());

        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        log.info("Deleting Onboarding Task with ID: {}", taskId);

        OnboardingTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("OnboardingTask", "id", taskId));

        taskRepository.delete(task);
        log.info("Onboarding Task deleted successfully with ID: {}", taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public OnboardingTaskDTO getTaskById(Long taskId) {
        log.info("Fetching Onboarding Task with ID: {}", taskId);

        OnboardingTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("OnboardingTask", "id", taskId));

        return taskMapper.toDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnboardingTaskDTO> getTasksByCustomerId(Long customerId) {
        log.info("Fetching all Onboarding Tasks for customer: {}", customerId);

        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        List<OnboardingTask> tasks = taskRepository.findByCustomerId(customerId);
        return taskMapper.toDTOList(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnboardingTaskDTO> getTasksByCustomerIdAndPlatform(Long customerId, OnboardingTask.Platform platform) {
        log.info("Fetching Onboarding Tasks for customer: {} and platform: {}", customerId, platform);

        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        List<OnboardingTask> tasks = taskRepository.findByCustomerIdAndPlatform(customerId, platform);
        return taskMapper.toDTOList(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCompletedTasksByCustomerId(Long customerId) {
        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        return taskRepository.countByCustomerIdAndStatus(customerId, OnboardingTask.TaskStatus.COMPLETED);
    }
}