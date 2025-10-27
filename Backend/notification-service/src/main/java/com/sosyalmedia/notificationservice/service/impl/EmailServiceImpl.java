package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import com.sosyalmedia.notificationservice.dto.request.BulkEmailRequestDTO;
import com.sosyalmedia.notificationservice.dto.request.EmailSendRequestDTO;
import com.sosyalmedia.notificationservice.exception.EmailSendException;
import com.sosyalmedia.notificationservice.service.EmailService;
import com.sosyalmedia.notificationservice.service.EmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    private final CustomerServiceClient customerServiceClient;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false = plain text

            mailSender.send(message);

            log.info("✉️ E-posta gönderildi: {} → {}", fromEmail, to);

        } catch (MessagingException e) {
            log.error("❌ E-posta gönderimi başarısız: {}", e.getMessage(), e);
            throw new EmailSendException("E-posta gönderilemedi: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendEmail(EmailSendRequestDTO request) {
        if (request.getTemplateKey() != null) {
            // Şablon kullan
            sendTemplatedEmail(request.getRecipientEmail(), request.getTemplateKey(), request.getVariables());
        } else {
            // Normal e-posta
            if (request.getBody().contains("<html>") || request.getBody().contains("<body>")) {
                sendHtmlEmail(request.getRecipientEmail(), request.getSubject(), request.getBody());
            } else {
                sendEmail(request.getRecipientEmail(), request.getSubject(), request.getBody());
            }
        }
    }

    @Override
    public void sendTemplatedEmail(String to, String templateKey, Map<String, String> variables) {
        try {
            log.info("📧 Şablonlu e-posta gönderiliyor: {} → {}", templateKey, to);

            // Şablonu işle
            String subject = emailTemplateService.processSubjectTemplate(templateKey, variables);
            String body = emailTemplateService.processBodyTemplate(templateKey, variables);

            // HTML e-posta gönder
            sendHtmlEmail(to, subject, body);

        } catch (Exception e) {
            log.error("❌ Şablonlu e-posta gönderilemedi: {}", e.getMessage(), e);
            throw new EmailSendException("Şablonlu e-posta gönderilemedi: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendBulkEmail(BulkEmailRequestDTO request) {
        log.info("📧 Toplu e-posta gönderimi başlatılıyor: {} müşteri", request.getCustomerIds().size());

        int successCount = 0;
        int failCount = 0;

        for (Long customerId : request.getCustomerIds()) {
            try {
                // Müşteri bilgilerini al
                CustomerDTO customer = customerServiceClient.getCustomerById(customerId);

                if (customer == null || customer.getPrimaryContact() == null) {
                    log.warn("⚠️ Müşteri bilgisi bulunamadı - ID: {}", customerId);
                    failCount++;
                    continue;
                }

                String recipientEmail = customer.getPrimaryContact().getEmail();

                // Şablon kullanılacak mı?
                if (request.getTemplateKey() != null) {
                    // Müşteri değişkenlerini ekle
                    Map<String, String> variables = request.getCommonVariables();
                    variables.put("companyName", customer.getCompanyName());
                    variables.put("contactName", customer.getPrimaryContact().getFullName());

                    sendTemplatedEmail(recipientEmail, request.getTemplateKey(), variables);
                } else {
                    sendHtmlEmail(recipientEmail, request.getSubject(), request.getBody());
                }

                successCount++;
                log.debug("✅ E-posta gönderildi: {}", recipientEmail);

            } catch (Exception e) {
                failCount++;
                log.error("❌ E-posta gönderilemedi - Customer ID: {}, Hata: {}", customerId, e.getMessage());
            }
        }

        log.info("✅ Toplu e-posta tamamlandı: {} başarılı, {} başarısız", successCount, failCount);
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(message);

            log.info("✉️ HTML e-posta gönderildi: {} → {}", fromEmail, to);

        } catch (MessagingException e) {
            log.error("❌ HTML e-posta gönderimi başarısız: {}", e.getMessage(), e);
            throw new EmailSendException("HTML e-posta gönderilemedi: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendTestEmail(String to) {
        String subject = "🧪 Test E-postası - Notification Service";
        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px;">
                <h2 style="color: #2563eb;">🧪 Test E-postası</h2>
                <p>Bu bir test e-postasıdır.</p>
                <p>E-posta servisi başarıyla çalışıyor! ✅</p>
                <hr>
                <p style="color: #6b7280; font-size: 12px;">
                    Sosyal Medya Yönetim Sistemi<br>
                    Notification Service
                </p>
            </body>
            </html>
            """;

        sendHtmlEmail(to, subject, body);
        log.info("🧪 Test e-postası gönderildi: {}", to);
    }
}