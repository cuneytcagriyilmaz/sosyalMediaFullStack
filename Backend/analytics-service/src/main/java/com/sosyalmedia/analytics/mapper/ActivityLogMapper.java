// src/main/java/com/sosyalmedia/analytics/mapper/ActivityLogMapper.java

package com.sosyalmedia.analytics.mapper;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.entity.ActivityLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityLogMapper {

    // Entity → DTO
    public ActivityLogDTO toDTO(ActivityLog entity) {
        if (entity == null) {
            return null;
        }

        return ActivityLogDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .customerName(null) // Sonradan set edilecek (customer service'den çekilecek)
                .activityType(entity.getActivityType())
                .message(entity.getMessage())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // DTO → Entity
    public ActivityLog toEntity(ActivityLogDTO dto) {
        if (dto == null) {
            return null;
        }

        ActivityLog entity = new ActivityLog();
        entity.setCustomerId(dto.getCustomerId());
        entity.setActivityType(dto.getActivityType());
        entity.setMessage(dto.getMessage());
        entity.setIcon(dto.getIcon());
        // createdAt otomatik set edilecek
        return entity;
    }

    // List<Entity> → List<DTO>
    public List<ActivityLogDTO> toDTOList(List<ActivityLog> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}