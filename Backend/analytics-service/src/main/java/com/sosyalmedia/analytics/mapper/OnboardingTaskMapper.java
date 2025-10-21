// src/main/java/com/sosyalmedia/analytics/mapper/OnboardingTaskMapper.java

package com.sosyalmedia.analytics.mapper;

import com.sosyalmedia.analytics.dto.OnboardingTaskDTO;
import com.sosyalmedia.analytics.entity.OnboardingTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OnboardingTaskMapper {

    // Entity → DTO
    public OnboardingTaskDTO toDTO(OnboardingTask entity) {
        if (entity == null) {
            return null;
        }

        return OnboardingTaskDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .taskName(entity.getTaskName())
                .taskType(entity.getTaskType())
                .platform(entity.getPlatform())
                .status(entity.getStatus())
                .connectionStatus(entity.getConnectionStatus())
                .connectionDate(entity.getConnectionDate())
                .notes(entity.getNotes())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // DTO → Entity (CREATE için)
    public OnboardingTask toEntity(OnboardingTaskDTO dto) {
        if (dto == null) {
            return null;
        }

        OnboardingTask entity = new OnboardingTask();
        entity.setCustomerId(dto.getCustomerId());
        entity.setTaskName(dto.getTaskName());
        entity.setTaskType(dto.getTaskType());
        entity.setPlatform(dto.getPlatform());
        entity.setStatus(dto.getStatus());
        entity.setConnectionStatus(dto.getConnectionStatus());
        entity.setConnectionDate(dto.getConnectionDate());
        entity.setNotes(dto.getNotes());
        entity.setStartedAt(dto.getStartedAt());
        entity.setCompletedAt(dto.getCompletedAt());
        // createdAt, updatedAt otomatik set edilecek
        return entity;
    }

    // UPDATE için - sadece değişen alanları güncelle
    public void updateEntityFromDTO(OnboardingTaskDTO dto, OnboardingTask entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getTaskName() != null) {
            entity.setTaskName(dto.getTaskName());
        }
        if (dto.getTaskType() != null) {
            entity.setTaskType(dto.getTaskType());
        }
        if (dto.getPlatform() != null) {
            entity.setPlatform(dto.getPlatform());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getConnectionStatus() != null) {
            entity.setConnectionStatus(dto.getConnectionStatus());
        }
        if (dto.getConnectionDate() != null) {
            entity.setConnectionDate(dto.getConnectionDate());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        if (dto.getStartedAt() != null) {
            entity.setStartedAt(dto.getStartedAt());
        }
        if (dto.getCompletedAt() != null) {
            entity.setCompletedAt(dto.getCompletedAt());
        }
        // updatedAt otomatik güncellenir (@UpdateTimestamp)
    }

    // List<Entity> → List<DTO>
    public List<OnboardingTaskDTO> toDTOList(List<OnboardingTask> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}