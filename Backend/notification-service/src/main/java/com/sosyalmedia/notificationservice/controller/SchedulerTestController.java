package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.scheduler.DeadlineNotificationScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications/scheduler")
@RequiredArgsConstructor
public class SchedulerTestController {

    private final DeadlineNotificationScheduler scheduler;

    @PostMapping("/trigger-daily")
    public ResponseEntity<String> triggerDaily() {
        scheduler.sendDailyDeadlineNotifications();
        return ResponseEntity.ok("✅ Daily notification scheduler triggered");
    }

    @PostMapping("/trigger-overdue")
    public ResponseEntity<String> triggerOverdue() {
        scheduler.checkOverdueDeadlines();
        return ResponseEntity.ok("✅ Overdue check scheduler triggered");
    }

    @PostMapping("/trigger-archive")
    public ResponseEntity<String> triggerArchive() {
        scheduler.archiveOldDeadlines();
        return ResponseEntity.ok("✅ Archive check scheduler triggered");
    }
}