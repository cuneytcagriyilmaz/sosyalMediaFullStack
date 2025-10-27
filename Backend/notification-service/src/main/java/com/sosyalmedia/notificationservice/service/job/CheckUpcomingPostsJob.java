package com.sosyalmedia.notificationservice.service.job;

import com.sosyalmedia.notificationservice.client.AnalyticsServiceClient;
import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.dto.external.ActivityDTO;
import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.model.NotificationSettings;
import com.sosyalmedia.notificationservice.service.EmailService;
import com.sosyalmedia.notificationservice.service.MockPostService;
import com.sosyalmedia.notificationservice.service.NotificationService;
import com.sosyalmedia.notificationservice.service.NotificationSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckUpcomingPostsJob implements Job {

    private final MockPostService mockPostService;
    private final NotificationService notificationService;
    private final NotificationSettingsService notificationSettingsService;
    private final EmailService emailService; // ✅ DÜZELT
    private final CustomerServiceClient customerServiceClient;
    private final AnalyticsServiceClient analyticsServiceClient;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("⏰ CheckUpcomingPostsJob çalışıyor...");

        try {
            NotificationSettings settings = notificationSettingsService
                    .getSettingByKey("UPCOMING_POST_REMINDERS");

            if (!settings.getIsActive()) {
                log.info("⚠️ Yaklaşan post hatırlatmaları devre dışı");
                return;
            }

            List<Integer> reminderDays = settings.getReminderDays();
            int totalNotifications = 0;

            for (Integer days : reminderDays) {
                LocalDateTime targetDate = LocalDateTime.now().plusDays(days);
                LocalDateTime startDate = targetDate.withHour(0).withMinute(0);
                LocalDateTime endDate = targetDate.withHour(23).withMinute(59);

                List<MockScheduledPost> posts = mockPostService.getPostsByDateRange(startDate, endDate);

                log.info("📅 {} gün sonrası için {} post bulundu", days, posts.size());

                for (MockScheduledPost post : posts) {
                    if (isNotificationAlreadySent(post.getId(), days)) {
                        continue;
                    }

                    CustomerDTO customer = customerServiceClient.getCustomerById(post.getCustomerId());

                    if (customer == null || customer.getPrimaryContact() == null) {
                        log.warn("⚠️ Müşteri bilgisi bulunamadı - Post ID: {}", post.getId());
                        continue;
                    }

                    Notification notification = createNotification(post, customer, days);
                    notificationService.createNotification(notification);

                    sendEmailNotification(post, customer, days);
                    saveActivity(post.getCustomerId(), days);

                    totalNotifications++;
                }
            }

            log.info("✅ CheckUpcomingPostsJob tamamlandı - {} bildirim gönderildi", totalNotifications);

        } catch (Exception e) {
            log.error("❌ CheckUpcomingPostsJob hatası: {}", e.getMessage(), e);
        }
    }

    private Notification createNotification(MockScheduledPost post, CustomerDTO customer, int daysRemaining) {
        String severity = determineSeverity(daysRemaining, post.getPreparationStatus());
        String icon = determineIcon(daysRemaining);

        String title = String.format("[%s] %d Gün Sonra Post!",
                customer.getCompanyName(), daysRemaining);

        String message = String.format(
                "%s için %d gün sonra (%s %s) post yayınlanacak. Durum: %s",
                customer.getCompanyName(),
                daysRemaining,
                post.getScheduledDate().toLocalDate(),
                post.getScheduledDate().toLocalTime(),
                getPreparationStatusText(post.getPreparationStatus())
        );

        return Notification.builder()
                .customerId(post.getCustomerId())
                .postId(post.getId())
                .notificationType("UPCOMING_POST")
                .title(title)
                .message(message)
                .icon(icon)
                .severity(severity)
                .isRead(false)
                .emailSent(false)
                .emailStatus("PENDING")
                .build();
    }

    private void sendEmailNotification(MockScheduledPost post, CustomerDTO customer, int daysRemaining) {
        try {
            String recipientEmail = customer.getPrimaryContact().getEmail();

            Map<String, String> variables = new HashMap<>();
            variables.put("companyName", customer.getCompanyName());
            variables.put("contactName", customer.getPrimaryContact().getFullName());
            variables.put("daysRemaining", String.valueOf(daysRemaining));
            variables.put("postDate", post.getScheduledDate().toLocalDate().toString());
            variables.put("postTime", post.getScheduledDate().toLocalTime().toString());
            variables.put("platforms", post.getPlatforms());
            variables.put("preparationStatus", getPreparationStatusText(post.getPreparationStatus()));

            String templateKey = daysRemaining <= 3 ? "CRITICAL_POST_ALERT" : "UPCOMING_POST_REMINDER";

            emailService.sendTemplatedEmail(recipientEmail, templateKey, variables); // ✅ DÜZELT

            log.info("📧 E-posta gönderildi: {} - {} gün kaldı", customer.getCompanyName(), daysRemaining);

        } catch (Exception e) {
            log.error("❌ E-posta gönderilemedi: {}", e.getMessage());
        }
    }

    private void saveActivity(Long customerId, int daysRemaining) {
        try {
            ActivityDTO activity = ActivityDTO.builder()
                    .customerId(customerId)
                    .activityType("NOTIFICATION_SENT")
                    .message(String.format("%d gün sonra post hatırlatması gönderildi", daysRemaining))
                    .icon("🔔")
                    .activityDate(LocalDateTime.now())
                    .build();

            analyticsServiceClient.createActivity(activity);

        } catch (Exception e) {
            log.warn("⚠️ Activity kaydedilemedi: {}", e.getMessage());
        }
    }

    private boolean isNotificationAlreadySent(Long postId, int daysRemaining) {
        return false;
    }

    private String determineSeverity(int daysRemaining, String preparationStatus) {
        if (daysRemaining <= 1) {
            return "CRITICAL";
        }
        if (daysRemaining <= 3 && !"READY".equals(preparationStatus)) {
            return "CRITICAL";
        }
        if (daysRemaining <= 7) {
            return "WARNING";
        }
        return "INFO";
    }

    private String determineIcon(int daysRemaining) {
        if (daysRemaining <= 1) return "🚨";
        if (daysRemaining <= 3) return "⚠️";
        if (daysRemaining <= 7) return "⏰";
        return "📅";
    }

    private String getPreparationStatusText(String status) {
        return switch (status) {
            case "NOT_STARTED" -> "Başlanmadı";
            case "IN_PROGRESS" -> "Devam Ediyor";
            case "READY" -> "Hazır";
            case "SENT" -> "Gönderildi";
            default -> status;
        };
    }
}