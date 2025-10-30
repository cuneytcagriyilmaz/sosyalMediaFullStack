package com.sosyalmedia.notificationservice.scheduler;

import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.repository.PostDeadlineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadlineNotificationScheduler {

    private final PostDeadlineRepository postDeadlineRepository;

    /**
     * Her gün saat 09:00'da çalışır
     * Bugün ve yarın için deadline'ları kontrol eder
     */
    @Scheduled(cron = "0 0 9 * * *")  // Her gün 09:00
    @Transactional
    public void sendDailyDeadlineNotifications() {
        log.info("🔔 [SCHEDULER] Starting daily deadline notifications...");

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Bugün ve yarın için bildirim gönderilmemiş deadline'ları bul
        List<PostDeadline> upcomingDeadlines = postDeadlineRepository
                .findByScheduledDateAndNotNotificationSent(today);

        List<PostDeadline> tomorrowDeadlines = postDeadlineRepository
                .findByScheduledDateAndNotNotificationSent(tomorrow);

        // Bugünkü deadline'lar
        for (PostDeadline deadline : upcomingDeadlines) {
            sendNotification(deadline, "URGENT");
            deadline.setNotificationSent(true);
            deadline.setNotificationSentAt(LocalDateTime.now());
            postDeadlineRepository.save(deadline);
        }

        // Yarınki deadline'lar
        for (PostDeadline deadline : tomorrowDeadlines) {
            sendNotification(deadline, "WARNING");
            deadline.setNotificationSent(true);
            deadline.setNotificationSentAt(LocalDateTime.now());
            postDeadlineRepository.save(deadline);
        }

        log.info("✅ [SCHEDULER] Sent {} notifications (Today: {}, Tomorrow: {})",
                upcomingDeadlines.size() + tomorrowDeadlines.size(),
                upcomingDeadlines.size(),
                tomorrowDeadlines.size());
    }

    /**
     * Her gün saat 10:00'da çalışır
     * Gecikmiş deadline'ları kontrol eder ve uyarı gönderir
     */
    @Scheduled(cron = "0 0 10 * * *")  // Her gün 10:00
    @Transactional(readOnly = true)
    public void checkOverdueDeadlines() {
        log.info("⏰ [SCHEDULER] Checking overdue deadlines...");

        LocalDate today = LocalDate.now();
        List<PostDeadline> overdueDeadlines = postDeadlineRepository.findOverdueDeadlines(today);

        if (!overdueDeadlines.isEmpty()) {
            log.warn("⚠️ [SCHEDULER] Found {} overdue deadlines!", overdueDeadlines.size());

            for (PostDeadline deadline : overdueDeadlines) {
                log.warn("⚠️ Overdue: Customer {}, Date: {}, Days overdue: {}",
                        deadline.getCustomerId(),
                        deadline.getScheduledDate(),
                        Math.abs(deadline.getDaysRemaining()));

                sendNotification(deadline, "OVERDUE");
            }
        } else {
            log.info("✅ [SCHEDULER] No overdue deadlines found");
        }
    }

    /**
     * Her Pazar saat 00:00'da çalışır
     * 90 günden eski ve tamamlanmış deadline'ları listeler (arşivleme için)
     */
    @Scheduled(cron = "0 0 0 * * SUN")  // Her Pazar 00:00
    @Transactional(readOnly = true)
    public void archiveOldDeadlines() {
        log.info("📦 [SCHEDULER] Checking old deadlines for archiving...");

        LocalDate cutoffDate = LocalDate.now().minusDays(90);
        List<PostDeadline> oldDeadlines = postDeadlineRepository.findExpiredDeadlines(cutoffDate);

        if (!oldDeadlines.isEmpty()) {
            log.info("📦 [SCHEDULER] Found {} old deadlines eligible for archiving", oldDeadlines.size());

            for (PostDeadline deadline : oldDeadlines) {
                log.debug("📦 Archivable deadline: ID={}, Customer={}, Date={}, Status={}",
                        deadline.getId(),
                        deadline.getCustomerId(),
                        deadline.getScheduledDate(),
                        deadline.getStatus());
            }

            log.info("ℹ️ [SCHEDULER] Use POST /api/v1/notifications/archive/{deadlineId} to manually archive these deadlines");
        } else {
            log.info("✅ [SCHEDULER] No old deadlines to archive");
        }
    }

    /**
     * Bildirim gönderme (şimdilik log, ileride Email/SMS/Slack)
     */
    private void sendNotification(PostDeadline deadline, String urgencyLevel) {
        log.info("📧 [NOTIFICATION] {} notification for deadline - ID: {}, Customer: {}, Date: {}, Platform: {}",
                urgencyLevel,
                deadline.getId(),
                deadline.getCustomerId(),
                deadline.getScheduledDate(),
                deadline.getPlatform() != null ? deadline.getPlatform().getDisplayName() : "N/A");

        // TODO: Email/SMS/Slack entegrasyonu
        // emailService.sendDeadlineNotification(deadline);
        // slackService.sendMessage(deadline);
    }
}