package com.sosyalmedia.notificationservice.dto.request;

import com.sosyalmedia.notificationservice.entity.Platform; // ✅ YENİ IMPORT
import com.sosyalmedia.notificationservice.entity.PostDeadline.PostDeadlineStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDeadlineCreateRequest {

    @NotNull(message = "Customer ID zorunludur")
    private Long customerId;

    @NotNull(message = "Tarih zorunludur")
    @FutureOrPresent(message = "Tarih bugün veya gelecekte olmalıdır")
    private LocalDate scheduledDate;

    @Builder.Default
    private PostDeadlineStatus status = PostDeadlineStatus.NOT_STARTED;

    @Builder.Default
    private Boolean contentReady = false;

    private String postContent;

    private Platform platform; // String -> Platform
}