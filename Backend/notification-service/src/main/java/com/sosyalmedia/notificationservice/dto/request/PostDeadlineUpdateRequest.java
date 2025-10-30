package com.sosyalmedia.notificationservice.dto.request;

import com.sosyalmedia.notificationservice.entity.Platform; // ✅ YENİ IMPORT
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDeadlineUpdateRequest {

    private LocalDate scheduledDate;

    private PostDeadlineStatus status;

    private Boolean contentReady;

    private String postContent;

    private Platform platform; // String -> Platform
}