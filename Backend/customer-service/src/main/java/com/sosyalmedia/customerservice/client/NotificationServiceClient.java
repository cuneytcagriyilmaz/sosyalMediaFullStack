package com.sosyalmedia.customerservice.client;

import com.sosyalmedia.customerservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "notification-service",
        url = "${notification-service.url:http://localhost:8082}"
)
public interface NotificationServiceClient {

    @PostMapping("/api/v1/notifications/auto-schedule/{customerId}")
    ApiResponse<Object> createAutoSchedule(@PathVariable("customerId") Long customerId);
}