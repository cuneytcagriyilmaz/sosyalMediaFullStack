package com.sosyalmedia.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sosyalmedia.notificationservice.entity.Platform; // ✅ YENİ IMPORT
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
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
public class PostDeadlineArchiveResponse {

    private Long id;

    private Long originalDeadlineId;

    // Customer bilgileri
    private Long customerId;
    private String companyName;
    private String sector;

    // Deadline bilgileri
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledDate;

    private PostDeadlineStatus finalStatus;
    private String statusDisplayName;
    private String statusColorCode;

    private Boolean contentReady;
    private String postContent;

    private Platform platform; // String -> Platform

    // Platform display bilgileri
    private String platformDisplayName;
    private String platformBrandColor;

    // Arşiv bilgileri
    private String archivedReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime archivedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime originalCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime originalUpdatedAt;
}