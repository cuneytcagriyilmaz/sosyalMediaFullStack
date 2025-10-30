package com.sosyalmedia.customerservice.service;

import com.sosyalmedia.customerservice.client.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationAsyncService {

    private final NotificationServiceClient notificationServiceClient;

    @Async
    public void triggerAutoSchedule(Long customerId) {
        try {
            log.info("📅 [ASYNC] Triggering auto-schedule for customer: {}", customerId);

            // ✅ Küçük bir delay ekle (transaction commit bekle)
            Thread.sleep(500);

            notificationServiceClient.createAutoSchedule(customerId);
            log.info("✅ [ASYNC] Auto-schedule triggered successfully for customer: {}", customerId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("❌ [ASYNC] Thread interrupted for customer: {}", customerId);
        } catch (Exception e) {
            log.error("❌ [ASYNC] Failed to trigger auto-schedule for customer {}: {}",
                    customerId, e.getMessage());
        }
    }
}