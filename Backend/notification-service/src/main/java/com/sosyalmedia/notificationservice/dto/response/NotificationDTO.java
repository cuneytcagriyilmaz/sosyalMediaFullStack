package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long customerId;
    private String companyName;
    private Long postId;
    private Long specialDateId;
    private String notificationType;
    private String title;
    private String message;
    private String icon;
    private String severity;
    private Boolean isRead;
    private LocalDateTime readAt;
    private Boolean emailSent;
    private LocalDateTime emailSentAt;
    private String emailStatus;
    private LocalDateTime createdAt;
}