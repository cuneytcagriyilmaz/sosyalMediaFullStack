package com.sosyalmedia.notificationservice.service.job;

import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.repository.NotificationRepository;
import com.sosyalmedia.notificationservice.service.EmailService;
import com.sosyalmedia.notificationservice.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendEmailNotificationsJob implements Job {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final CustomerServiceClient customerServiceClient;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("⏰ SendEmailNotificationsJob çalışıyor...");

        try {
            // E-posta gönderilmemiş bildirimleri al
            List<Notification> pendingNotifications = notificationRepository.findByEmailSentFalse();

            log.info("📧 {} e-posta gönderilecek", pendingNotifications.size());

            int successCount = 0;
            int failCount = 0;

            for (Notification notification : pendingNotifications) {
                try {
                    // Müşteri bilgilerini al
                    CustomerDTO customer = customerServiceClient.getCustomerById(notification.getCustomerId());

                    if (customer == null || customer.getPrimaryContact() == null) {
                        log.warn("⚠️ Müşteri bilgisi bulunamadı - Notification ID: {}", notification.getId());
                        notification.setEmailStatus("FAILED");
                        notificationRepository.save(notification);
                        failCount++;
                        continue;
                    }

                    String recipientEmail = customer.getPrimaryContact().getEmail();

                    // E-posta gönder
                    emailService.sendHtmlEmail(
                            recipientEmail,
                            notification.getTitle(),
                            buildEmailBody(notification, customer)
                    );

                    // Bildirim durumunu güncelle
                    notification.setEmailSent(true);
                    notification.setEmailSentAt(LocalDateTime.now());
                    notification.setEmailStatus("SENT");
                    notificationRepository.save(notification);

                    successCount++;
                    log.debug("✅ E-posta gönderildi: {} - Notification ID: {}",
                            recipientEmail, notification.getId());

                } catch (Exception e) {
                    log.error("❌ E-posta gönderilemedi - Notification ID: {}, Hata: {}",
                            notification.getId(), e.getMessage());

                    notification.setEmailStatus("FAILED");
                    notificationRepository.save(notification);
                    failCount++;
                }
            }

            log.info("✅ SendEmailNotificationsJob tamamlandı - Başarılı: {}, Başarısız: {}",
                    successCount, failCount);

        } catch (Exception e) {
            log.error("❌ SendEmailNotificationsJob hatası: {}", e.getMessage(), e);
        }
    }

    /**
     * E-posta içeriği oluştur
     */
    private String buildEmailBody(Notification notification, CustomerDTO customer) {
        String severityColor = switch (notification.getSeverity()) {
            case "CRITICAL" -> "#dc2626";
            case "WARNING" -> "#f59e0b";
            default -> "#2563eb";
        };

        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px;">
                <div style="border-left: 4px solid %s; padding-left: 15px; margin-bottom: 20px;">
                    <h2 style="color: %s; margin: 0;">%s %s</h2>
                </div>
                <p>Merhaba %s,</p>
                <p>%s</p>
                <div style="background-color: #f3f4f6; padding: 15px; border-radius: 5px; margin: 20px 0;">
                    <p style="margin: 0;"><strong>Bildirim Tipi:</strong> %s</p>
                    <p style="margin: 5px 0 0 0;"><strong>Tarih:</strong> %s</p>
                </div>
                <hr style="margin: 30px 0; border: none; border-top: 1px solid #e5e7eb;">
                <p style="color: #6b7280; font-size: 12px;">
                    Sosyal Medya Yönetim Sistemi<br>
                    Notification Service
                </p>
            </body>
            </html>
            """,
                severityColor,
                severityColor,
                notification.getIcon() != null ? notification.getIcon() : "📬",
                notification.getTitle(),
                customer.getPrimaryContact().getFullName(),
                notification.getMessage(),
                getNotificationTypeText(notification.getNotificationType()),
                notification.getCreatedAt()
        );
    }

    /**
     * Bildirim tipi metni
     */
    private String getNotificationTypeText(String type) {
        return switch (type) {
            case "UPCOMING_POST" -> "Yaklaşan Post Hatırlatması";
            case "OVERDUE_POST" -> "Gecikmiş Post Uyarısı";
            case "SPECIAL_DATE_REMINDER" -> "Özel Gün Hatırlatması";
            case "CRITICAL_ALERT" -> "Kritik Uyarı";
            default -> type;
        };
    }
}