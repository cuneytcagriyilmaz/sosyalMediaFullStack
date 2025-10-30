package com.sosyalmedia.notificationservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDTO {
    private Long customerId;
    private String activityType;  // DEADLINE_CREATED, DEADLINE_UPDATED, etc.
    private String message;
    private String icon;
}