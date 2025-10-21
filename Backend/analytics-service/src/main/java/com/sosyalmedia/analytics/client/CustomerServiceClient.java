// src/main/java/com/sosyalmedia/analytics/client/CustomerServiceClient.java

package com.sosyalmedia.analytics.client;

import com.sosyalmedia.analytics.client.dto.customerservice.ApiResponseDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        url = "${customer-service.url:http://localhost:8081}"
)
public interface CustomerServiceClient {

    /**
     * Customer-service'den müşteri bilgisini getir
     * GET /api/customers/{id}
     */
    @GetMapping("/api/customers/{id}")
    ApiResponseDTO<CustomerResponseDTO> getCustomerById(@PathVariable("id") Long id);
}