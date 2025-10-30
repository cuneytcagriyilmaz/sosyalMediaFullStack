package com.sosyalmedia.notificationservice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTargetAudienceDTO {
    private Long id;
    private Boolean specialDates;
    private String targetRegion;
    private String customerHashtags;
    private String postType;
    private Integer postFrequency;  // ✅ DEĞİŞTİ: String -> Integer (1-7)
    private String postTone;
    private String audienceAge;
    private String audienceInterests;
}