// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerTargetAudienceDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTargetAudienceDTO {
    private Long id;
    private Boolean specialDates;
    private String targetRegion;
    private String customerHashtags;
    private String postType;
    private String postFrequency;
    private String postTone;
    private String audienceAge;
    private String audienceInterests;
}