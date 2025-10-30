package com.sosyalmedia.notificationservice.client.dto;

import com.sosyalmedia.notificationservice.entity.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostBasicDTO {
    private Long id;
    private Long customerId;
    private String content;
    private Platform platform;
    private String status; // DRAFT, SCHEDULED, PUBLISHED
    private LocalDateTime scheduledDate;
    private LocalDateTime createdAt;
}