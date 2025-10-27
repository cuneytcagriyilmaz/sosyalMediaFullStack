package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.BulkEmailRequestDTO;
import com.sosyalmedia.notificationservice.dto.request.EmailSendRequestDTO;

import java.util.Map;

public interface EmailService {

    /**
     * E-posta gönder
     */
    void sendEmail(String to, String subject, String body);

    /**
     * DTO ile e-posta gönder
     */
    void sendEmail(EmailSendRequestDTO request);

    /**
     * Şablon kullanarak e-posta gönder
     */
    void sendTemplatedEmail(String to, String templateKey, Map<String, String> variables);

    /**
     * Toplu e-posta gönder
     */
    void sendBulkEmail(BulkEmailRequestDTO request);

    /**
     * HTML e-posta gönder
     */
    void sendHtmlEmail(String to, String subject, String htmlBody);

    /**
     * E-posta gönderimi test et
     */
    void sendTestEmail(String to);
}