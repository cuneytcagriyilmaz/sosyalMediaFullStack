// src/main/java/com/sosyalmedia/analytics/client/CustomerServiceClient.java

package com.sosyalmedia.analytics.client;

import com.sosyalmedia.analytics.client.dto.customerservice.ApiResponseDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerListDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "customer-service",
        url = "${customer-service.url:http://localhost:8081}"
)
public interface CustomerServiceClient {

    /**
     * Tek müşteri detayı getir
     * GET /api/customers/{id}
     * Response: CustomerResponseDTO (tüm detaylar)
     */
    @GetMapping("/api/customers/{id}")
    ApiResponseDTO<CustomerResponseDTO> getCustomerById(@PathVariable("id") Long id);

    /**
     * Tüm müşterilerin özet listesi
     * GET /api/customers
     * Response: List<CustomerListDTO> (sadece özet)
     */
    @GetMapping("/api/customers")
    ApiResponseDTO<List<CustomerListDTO>> getAllCustomers();
}