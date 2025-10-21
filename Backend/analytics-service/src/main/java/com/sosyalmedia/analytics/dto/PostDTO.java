// src/main/java/com/sosyalmedia/analytics/dto/PostDTO.java

package com.sosyalmedia.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;
    private String title;
    private LocalDateTime scheduledDate;
    private String platform;
    private String status;
    private String thumbnail;
}