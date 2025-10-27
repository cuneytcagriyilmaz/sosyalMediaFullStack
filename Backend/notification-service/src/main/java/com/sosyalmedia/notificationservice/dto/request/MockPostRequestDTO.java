package com.sosyalmedia.notificationservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockPostRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private String postType; // NORMAL, SPECIAL_DAY

    private Boolean isSpecialDayPost;

    private Long specialDateId;

    private String status; // SCHEDULED, READY, PUBLISHED

    private String preparationStatus; // NOT_STARTED, IN_PROGRESS, READY, SENT

    private String content;

    private String platforms; // Instagram,Facebook,Twitter

    private String notes;
}