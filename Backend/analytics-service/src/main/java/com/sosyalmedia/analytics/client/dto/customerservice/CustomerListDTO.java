// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerListDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListDTO {
    private Long id;
    private String companyName;
    private String sector;
    private String membershipPackage;
    private String status;  // ACTIVE, PASSIVE, CANCELLED
    private LocalDateTime createdAt;
}