// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/mapper/ActivityLogMapper.java

package com.sosyalmedia.analytics.mapper;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.entity.ActivityLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityLogMapper {

    /**
     * Entity -> DTO
     * ✅ Enum'ı String'e çevir
     */
    public ActivityLogDTO toDTO(ActivityLog entity) {
        if (entity == null) {
            return null;
        }

        return ActivityLogDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .activityType(entity.getActivityType().name()) // ✅ Enum -> String
                .message(entity.getMessage())
                .icon(entity.getIcon())
                .timestamp(entity.getCreatedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    /**
     * DTO -> Entity
     * ✅ String'i Enum'a çevir
     */
    public ActivityLog toEntity(ActivityLogDTO dto) {
        if (dto == null) {
            return null;
        }

        // ✅ String'i Enum'a çevir - hata kontrolü ile
        ActivityLog.ActivityType activityType;
        try {
            activityType = ActivityLog.ActivityType.valueOf(dto.getActivityType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz aktivite tipi: " + dto.getActivityType());
        }

        return ActivityLog.builder()
                .id(dto.getId())
                .customerId(dto.getCustomerId())
                .activityType(activityType) // ✅ String -> Enum
                .message(dto.getMessage())
                .icon(dto.getIcon())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .build();
    }

    /**
     * DTO -> Entity (Create için - ID olmadan)
     */
    public ActivityLog toEntityForCreate(ActivityLogDTO dto) {
        if (dto == null) {
            return null;
        }

        // ✅ String'i Enum'a çevir - hata kontrolü ile
        ActivityLog.ActivityType activityType;
        try {
            activityType = ActivityLog.ActivityType.valueOf(dto.getActivityType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz aktivite tipi: " + dto.getActivityType());
        }

        return ActivityLog.builder()
                .customerId(dto.getCustomerId())
                .activityType(activityType) // ✅ String -> Enum
                .message(dto.getMessage())
                .icon(dto.getIcon())
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Entity -> DTO (Müşteri adı ile birlikte)
     */
    public ActivityLogDTO toDTOWithCustomerName(ActivityLog entity, String customerName) {
        if (entity == null) {
            return null;
        }

        return ActivityLogDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .customerName(customerName) // ✅ Ek bilgi
                .activityType(entity.getActivityType().name()) // ✅ Enum -> String
                .message(entity.getMessage())
                .icon(entity.getIcon())
                .timestamp(entity.getCreatedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}