// src/main/java/com/sosyalmedia/analytics/service/impl/AIContentTaskServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.dto.AIContentTaskDTO;
import com.sosyalmedia.analytics.entity.AIContentTask;
import com.sosyalmedia.analytics.exception.ResourceNotFoundException;
import com.sosyalmedia.analytics.mapper.AIContentTaskMapper;
import com.sosyalmedia.analytics.repository.AIContentTaskRepository;
import com.sosyalmedia.analytics.service.AIContentTaskService;
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
public class AIContentTaskServiceImpl implements AIContentTaskService {

    private final AIContentTaskRepository taskRepository;
    private final AIContentTaskMapper taskMapper;
    private final CustomerValidationService customerValidationService; // ✅ YENİ

    @Override
    public AIContentTaskDTO createTask(AIContentTaskDTO taskDTO) {
        log.info("Creating new AI Content Task for customer: {}", taskDTO.getCustomerId());

        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(taskDTO.getCustomerId());

        AIContentTask task = taskMapper.toEntity(taskDTO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        AIContentTask savedTask = taskRepository.save(task);
        log.info("AI Content Task created successfully with ID: {}", savedTask.getId());

        return taskMapper.toDTO(savedTask);
    }

    @Override
    public AIContentTaskDTO updateTask(Long taskId, AIContentTaskDTO taskDTO) {
        log.info("Updating AI Content Task with ID: {}", taskId);

        AIContentTask existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("AIContentTask", "id", taskId));

        // ✅ VALIDATION: Eğer customerId değişiyorsa, yeni customer'ı kontrol et
        if (taskDTO.getCustomerId() != null &&
                !taskDTO.getCustomerId().equals(existingTask.getCustomerId())) {
            customerValidationService.validateCustomerExists(taskDTO.getCustomerId());
        }

        // Mapper ile güncelleme
        taskMapper.updateEntityFromDTO(taskDTO, existingTask);
        existingTask.setUpdatedAt(LocalDateTime.now());

        AIContentTask updatedTask = taskRepository.save(existingTask);
        log.info("AI Content Task updated successfully with ID: {}", updatedTask.getId());

        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        log.info("Deleting AI Content Task with ID: {}", taskId);

        AIContentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("AIContentTask", "id", taskId));

        taskRepository.delete(task);
        log.info("AI Content Task deleted successfully with ID: {}", taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public AIContentTaskDTO getTaskById(Long taskId) {
        log.info("Fetching AI Content Task with ID: {}", taskId);

        AIContentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("AIContentTask", "id", taskId));

        return taskMapper.toDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIContentTaskDTO> getTasksByCustomerId(Long customerId) {
        log.info("Fetching all AI Content Tasks for customer: {}", customerId);

        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        List<AIContentTask> tasks = taskRepository.findByCustomerId(customerId);
        return taskMapper.toDTOList(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AIContentTaskDTO> getTasksByStatus(AIContentTask.TaskStatus status) {
        log.info("Fetching all AI Content Tasks with status: {}", status);

        List<AIContentTask> tasks = taskRepository.findByStatus(status);
        return taskMapper.toDTOList(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTasksByCustomerId(Long customerId) {
        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        return taskRepository.countByCustomerId(customerId);
    }
}