// src/main/java/com/sosyalmedia/analytics/dto/PostStatsDTO.java

package com.sosyalmedia.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatsDTO {

    private Long totalGenerated;
    private Long ready;
    private Long sent;
    private Long scheduled;
    private Map<String, Long> byPlatform;
}