package com.sosyalmedia.notificationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFilterDTO {
    private Long customerId;
    private String notificationType;
    private String severity;
    private Boolean isRead;
    private LocalDate startDate;
    private LocalDate endDate;
}