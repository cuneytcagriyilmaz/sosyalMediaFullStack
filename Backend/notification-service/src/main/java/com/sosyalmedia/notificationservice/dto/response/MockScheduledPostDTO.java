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
public class MockScheduledPostDTO {
    private Long id;
    private Long customerId;
    private String companyName; // Customer service'den gelecek
    private LocalDateTime scheduledDate;
    private String scheduledTime; // "09:00"
    private String postType; // NORMAL, SPECIAL_DAY
    private Boolean isSpecialDayPost;
    private Long specialDateId;
    private String specialDateName;
    private String specialDateIcon;
    private String status;
    private String preparationStatus;
    private String content;
    private String platforms;
    private Long daysRemaining; // Kalan gün sayısı
    private String priorityColor; // danger, warning, info, success
}