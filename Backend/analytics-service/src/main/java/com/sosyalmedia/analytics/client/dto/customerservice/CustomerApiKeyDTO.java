// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerApiKeyDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerApiKeyDTO {
    private Long id;
    private String instagramApiKey;
    private String facebookApiKey;
    private String tiktokApiKey;
    private String googleApiKey;
}