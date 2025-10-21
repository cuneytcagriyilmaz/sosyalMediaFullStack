// src/main/java/com/sosyalmedia/analytics/dto/CustomerNoteDTO.java

package com.sosyalmedia.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNoteDTO {

    private Long id;
    private Long customerId;
    private String text;
    private LocalDateTime createdAt;
    private String createdBy;
}