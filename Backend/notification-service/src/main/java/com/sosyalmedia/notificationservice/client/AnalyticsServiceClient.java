package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.dto.external.ActivityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "analytics-service",
        fallback = AnalyticsServiceClientFallback.class
)
public interface AnalyticsServiceClient {

    /**
     * Tek aktivite oluştur
     */
    @PostMapping("/api/analytics/activities")
    void createActivity(@RequestBody ActivityDTO activity);

    /**
     * Toplu aktivite oluştur
     */
    @PostMapping("/api/analytics/activities/bulk")
    void createActivitiesBulk(@RequestBody List<ActivityDTO> activities);
}