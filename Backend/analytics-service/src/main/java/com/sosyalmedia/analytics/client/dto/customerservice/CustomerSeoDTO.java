// src/main/java/com/sosyalmedia/analytics/client/dto/customerservice/CustomerSeoDTO.java

package com.sosyalmedia.analytics.client.dto.customerservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSeoDTO {
    private Long id;
    private String googleConsoleEmail;
    private String titleSuggestions;
    private String contentSuggestions;
}