package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.client.dto.CustomerFullDTO;
import com.sosyalmedia.notificationservice.config.FeignConfig;
import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        path = "/api/customers",
        configuration = FeignConfig.class
)
public interface CustomerServiceClient {

    // ✅ MEVCUT - Basic info için
    @GetMapping("/{id}")
    ApiResponse<CustomerBasicDTO> getCustomerById(@PathVariable("id") Long id);

    // ✅ YENİ - Full data için (aynı endpoint, farklı DTO)
    @GetMapping("/{id}")
    ApiResponse<CustomerFullDTO> getCustomerFullData(@PathVariable("id") Long id);
}