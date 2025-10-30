package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.client.dto.ActivityLogDTO;
import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "analytics-service", path = "/api/v1/analytics/activities")
public interface AnalyticsServiceClient {

    @PostMapping
    ApiResponse<ActivityLogDTO> createActivity(@RequestBody ActivityLogDTO activityDTO);
}