// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerContactDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContactDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private Integer priority;
}