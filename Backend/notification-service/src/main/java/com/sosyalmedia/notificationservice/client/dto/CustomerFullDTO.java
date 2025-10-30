package com.sosyalmedia.notificationservice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerFullDTO {
    private Long id;
    private String companyName;
    private String sector;
    private String status;
    private String membershipPackage;
    private LocalDateTime createdAt;  // ✅ İlk post hesaplama için

    // ✅ Nested objects
    private CustomerTargetAudienceDTO targetAudience;
    private CustomerSocialMediaDTO socialMedia;
}