// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerResponseDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String companyName;
    private String sector;
    private String address;
    private String membershipPackage;
    private String status;

    private CustomerTargetAudienceDTO targetAudience;
    private List<CustomerContactDTO> contacts;
    private CustomerSocialMediaDTO socialMedia;
    private CustomerSeoDTO seo;
    private CustomerApiKeyDTO apiKeys;
    private List<CustomerMediaDTO> media;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}