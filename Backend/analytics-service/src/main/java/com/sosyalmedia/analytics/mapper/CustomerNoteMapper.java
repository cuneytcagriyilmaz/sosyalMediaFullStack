// src/main/java/com/sosyalmedia/analytics/mapper/CustomerNoteMapper.java

package com.sosyalmedia.analytics.mapper;

import com.sosyalmedia.analytics.dto.CustomerNoteDTO;
import com.sosyalmedia.analytics.entity.CustomerNote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerNoteMapper {

    // Entity → DTO
    public CustomerNoteDTO toDTO(CustomerNote entity) {
        if (entity == null) {
            return null;
        }

        return CustomerNoteDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .text(entity.getText())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    // DTO → Entity
    public CustomerNote toEntity(CustomerNoteDTO dto) {
        if (dto == null) {
            return null;
        }

        CustomerNote entity = new CustomerNote();
        entity.setCustomerId(dto.getCustomerId());
        entity.setText(dto.getText());
        entity.setCreatedBy(dto.getCreatedBy());
        // createdAt otomatik set edilecek
        return entity;
    }

    // List<Entity> → List<DTO>
    public List<CustomerNoteDTO> toDTOList(List<CustomerNote> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}