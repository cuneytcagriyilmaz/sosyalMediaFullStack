package com.sosyalmedia.notificationservice.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long customerId;
    private String activityType; // NOTIFICATION_SENT, EMAIL_SENT, POST_REMINDER, etc.
    private String message;
    private String icon;
    private LocalDateTime activityDate;
}