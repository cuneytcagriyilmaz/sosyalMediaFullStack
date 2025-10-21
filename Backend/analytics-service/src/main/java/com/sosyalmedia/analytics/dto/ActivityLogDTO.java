// src/main/java/com/sosyalmedia/analytics/dto/ActivityLogDTO.java

package com.sosyalmedia.analytics.dto;

import com.sosyalmedia.analytics.entity.ActivityLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDTO {

    private Long id;
    private Long customerId;
    private String customerName;
    private ActivityLog.ActivityType activityType;
    private String message;
    private String icon;
    private LocalDateTime createdAt;
}