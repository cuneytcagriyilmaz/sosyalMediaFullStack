// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/dto/ActivityLogDTO.java

package com.sosyalmedia.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
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
public class ActivityLogDTO {

    private Long id;

    private Long customerId;

    private String customerName; // Müşteri adı (join ile gelecek)

    @NotBlank(message = "Activity type zorunludur")
    private String activityType;

    @NotBlank(message = "Message zorunludur")
    private String message;

    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}