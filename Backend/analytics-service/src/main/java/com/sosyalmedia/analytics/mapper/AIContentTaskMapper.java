// src/main/java/com/sosyalmedia/analytics/mapper/AIContentTaskMapper.java

package com.sosyalmedia.analytics.mapper;

import com.sosyalmedia.analytics.dto.AIContentTaskDTO;
import com.sosyalmedia.analytics.entity.AIContentTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AIContentTaskMapper {

    // Entity → DTO
    public AIContentTaskDTO toDTO(AIContentTask entity) {
        if (entity == null) {
            return null;
        }

        return AIContentTaskDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .taskName(entity.getTaskName())
                .taskType(entity.getTaskType())
                .status(entity.getStatus())
                .quantity(entity.getQuantity())
                .progressCurrent(entity.getProgressCurrent())
                .progressTotal(entity.getProgressTotal())
                .notes(entity.getNotes())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .dueDate(entity.getDueDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // DTO → Entity (CREATE için)
    public AIContentTask toEntity(AIContentTaskDTO dto) {
        if (dto == null) {
            return null;
        }

        AIContentTask entity = new AIContentTask();
        entity.setCustomerId(dto.getCustomerId());
        entity.setTaskName(dto.getTaskName());
        entity.setTaskType(dto.getTaskType());
        entity.setStatus(dto.getStatus());
        entity.setQuantity(dto.getQuantity());
        entity.setProgressCurrent(dto.getProgressCurrent());
        entity.setProgressTotal(dto.getProgressTotal());
        entity.setNotes(dto.getNotes());
        entity.setStartedAt(dto.getStartedAt());
        entity.setCompletedAt(dto.getCompletedAt());
        entity.setDueDate(dto.getDueDate());
        // createdAt, updatedAt otomatik set edilecek
        return entity;
    }

    // UPDATE için - sadece değişen alanları güncelle
    public void updateEntityFromDTO(AIContentTaskDTO dto, AIContentTask entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getTaskName() != null) {
            entity.setTaskName(dto.getTaskName());
        }
        if (dto.getTaskType() != null) {
            entity.setTaskType(dto.getTaskType());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getProgressCurrent() != null) {
            entity.setProgressCurrent(dto.getProgressCurrent());
        }
        if (dto.getProgressTotal() != null) {
            entity.setProgressTotal(dto.getProgressTotal());
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
        if (dto.getDueDate() != null) {
            entity.setDueDate(dto.getDueDate());
        }
        // updatedAt otomatik güncellenir (@UpdateTimestamp)
    }

    // List<Entity> → List<DTO>
    public List<AIContentTaskDTO> toDTOList(List<AIContentTask> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}