package com.sosyalmedia.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Servis sağlık kontrolü
        boolean isHealthy = checkServiceHealth();

        if (isHealthy) {
            return Health.up()
                    .withDetail("service", "notification-service")
                    .withDetail("status", "All systems operational")
                    .build();
        } else {
            return Health.down()
                    .withDetail("service", "notification-service")
                    .withDetail("error", "Service degraded")
                    .build();
        }
    }

    private boolean checkServiceHealth() {
        // Burada database, feign client vb. kontrol edilebilir
        return true;
    }
}