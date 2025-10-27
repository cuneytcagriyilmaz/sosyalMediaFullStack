package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.dto.external.ActivityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AnalyticsServiceClientFallback implements AnalyticsServiceClient {

    @Override
    public void createActivity(ActivityDTO activity) {
        log.warn("⚠️ AnalyticsService unavailable - Fallback: createActivity");
        log.debug("Failed activity: Type={}, CustomerId={}, Message={}",
                activity.getActivityType(),
                activity.getCustomerId(),
                activity.getMessage());
        // Activity kaydedilemedi ama sistem çalışmaya devam ediyor
    }

    @Override
    public void createActivitiesBulk(List<ActivityDTO> activities) {
        log.warn("⚠️ AnalyticsService unavailable - Fallback: createActivitiesBulk");
        log.debug("Failed to log {} activities", activities.size());
        // Toplu activity kaydedilemedi ama sistem çalışmaya devam ediyor
    }
}