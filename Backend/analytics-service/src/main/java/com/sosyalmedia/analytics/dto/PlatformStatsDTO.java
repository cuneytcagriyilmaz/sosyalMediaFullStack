// src/main/java/com/sosyalmedia/analytics/dto/PlatformStatsDTO.java

package com.sosyalmedia.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformStatsDTO {

    private Long connectedCustomers;
    private Long totalPosts;
    private Long sentPosts;
    private Long scheduledPosts;
    private Long readyPosts;
    private String color;
}