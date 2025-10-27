package com.sosyalmedia.notificationservice.service.job; // ✅ DOĞRU PACKAGE

import com.sosyalmedia.notificationservice.client.AnalyticsServiceClient; // ✅ DÜZELT
import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.dto.external.ActivityDTO;
import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.service.EmailService;
import com.sosyalmedia.notificationservice.service.EmailTemplateService;
import com.sosyalmedia.notificationservice.service.MockPostService;
import com.sosyalmedia.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckOverduePostsJob implements Job {

    private final MockPostService mockPostService;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final CustomerServiceClient customerServiceClient;
    private final AnalyticsServiceClient analyticsServiceClient; // ✅ DÜZELT

    @Override
    public void execute(JobExecutionContext context) {
        log.info("⏰ CheckOverduePostsJob çalışıyor...");

        try {
            List<MockScheduledPost> overduePosts = mockPostService.getOverduePosts();

            log.info("📅 {} gecikmiş post bulundu", overduePosts.size());

            int totalNotifications = 0;

            for (MockScheduledPost post : overduePosts) {
                CustomerDTO customer = customerServiceClient.getCustomerById(post.getCustomerId());

                if (customer == null || customer.getPrimaryContact() == null) {
                    log.warn("⚠️ Müşteri bilgisi bulunamadı - Post ID: {}", post.getId());
                    continue;
                }

                long daysOverdue = ChronoUnit.DAYS.between(
                        post.getScheduledDate().toLocalDate(),
                        LocalDateTime.now().toLocalDate()
                );

                if (daysOverdue == 0) {
                    daysOverdue = 1;
                }

                Notification notification = createNotification(post, customer, daysOverdue);
                notificationService.createNotification(notification);

                sendEmailNotification(post, customer, daysOverdue);
                saveActivity(post.getCustomerId(), daysOverdue);

                totalNotifications++;
            }

            log.info("✅ CheckOverduePostsJob tamamlandı - {} bildirim gönderildi", totalNotifications);

        } catch (Exception e) {
            log.error("❌ CheckOverduePostsJob hatası: {}", e.getMessage(), e);
        }
    }

    private Notification createNotification(MockScheduledPost post, CustomerDTO customer, long daysOverdue) {
        String title = String.format("🚨 [%s] Post Tarihi Geçti!", customer.getCompanyName());

        String message = String.format(
                "%s postu %d gün önce yayınlanmalıydı (%s). Durum: %s - Lütfen acilen postu yayınlayın veya yeni tarih planlayın!",
                customer.getCompanyName(),
                daysOverdue,
                post.getScheduledDate().toLocalDate(),
                getStatusText(post.getStatus())
        );

        return Notification.builder()
                .customerId(post.getCustomerId())
                .postId(post.getId())
                .notificationType("OVERDUE_POST")
                .title(title)
                .message(message)
                .icon("🚨")
                .severity("CRITICAL")
                .isRead(false)
                .emailSent(false)
                .emailStatus("PENDING")
                .build();
    }

    private void sendEmailNotification(MockScheduledPost post, CustomerDTO customer, long daysOverdue) {
        try {
            String recipientEmail = customer.getPrimaryContact().getEmail();

            Map<String, String> variables = new HashMap<>();
            variables.put("companyName", customer.getCompanyName());
            variables.put("contactName", customer.getPrimaryContact().getFullName());
            variables.put("postDate", post.getScheduledDate().toLocalDate().toString());
            variables.put("daysOverdue", String.valueOf(daysOverdue));

            emailService.sendTemplatedEmail(recipientEmail, "OVERDUE_POST_ALERT", variables);

            log.info("📧 Gecikme e-postası gönderildi: {} - {} gün gecikmiş",
                    customer.getCompanyName(), daysOverdue);

        } catch (Exception e) {
            log.error("❌ E-posta gönderilemedi: {}", e.getMessage());
        }
    }

    private void saveActivity(Long customerId, long daysOverdue) {
        try {
            ActivityDTO activity = ActivityDTO.builder()
                    .customerId(customerId)
                    .activityType("OVERDUE_ALERT")
                    .message(String.format("Gecikmiş post uyarısı gönderildi (%d gün)", daysOverdue))
                    .icon("🚨")
                    .activityDate(LocalDateTime.now())
                    .build();

            analyticsServiceClient.createActivity(activity); // ✅ DÜZELT
        } catch (Exception e) {
            log.warn("⚠️ Activity kaydedilemedi: {}", e.getMessage());
        }
    }

    private String getStatusText(String status) {
        return switch (status) {
            case "SCHEDULED" -> "Planlandı";
            case "READY" -> "Hazır";
            case "PUBLISHED" -> "Yayınlandı";
            default -> status;
        };
    }
}