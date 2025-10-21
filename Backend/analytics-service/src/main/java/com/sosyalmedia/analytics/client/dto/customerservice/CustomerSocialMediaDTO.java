// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerSocialMediaDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSocialMediaDTO {
    private Long id;
    private String instagram;
    private String facebook;
    private String tiktok;
}