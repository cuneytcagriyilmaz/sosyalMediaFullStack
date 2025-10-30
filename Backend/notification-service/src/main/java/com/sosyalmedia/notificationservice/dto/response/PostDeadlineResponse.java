package com.sosyalmedia.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sosyalmedia.notificationservice.entity.Platform;
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import com.sosyalmedia.notificationservice.entity.PostDeadline.UrgencyLevel;
import com.sosyalmedia.notificationservice.entity.PostDeadline.EventType; // ✅ YENİ
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDeadlineResponse {

    private Long id;

    private Long customerId;
    private String companyName;
    private String sector;
    private String customerStatus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledDate;

    private Integer daysRemaining;

    private UrgencyLevel urgencyLevel;
    private String urgencyColor;
    private String urgencyDisplayName;

    private PostDeadlineStatus status;
    private String statusDisplayName;
    private String statusColorCode;

    private Boolean contentReady;

    private String postContent;

    private Platform platform;
    private String platformDisplayName;
    private String platformBrandColor;

    //  YENİ: EventType bilgileri (opsiyonel)
    private EventType eventType;
    private String eventTypeDisplayName;
    private String eventTypeColorCode;

    //  YENİ: Holiday bilgileri (sadece SPECIAL_DATE için dolu)
    private String holidayName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}