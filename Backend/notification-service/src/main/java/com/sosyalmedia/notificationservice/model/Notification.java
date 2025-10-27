package com.sosyalmedia.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "special_date_id")
    private Long specialDateId;

    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType; // UPCOMING_POST, OVERDUE_POST, SPECIAL_DATE_REMINDER, CRITICAL_ALERT

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "icon", length = 10)
    private String icon;

    @Column(name = "severity", length = 20)
    private String severity; // INFO, WARNING, CRITICAL

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "email_sent")
    private Boolean emailSent;

    @Column(name = "email_sent_at")
    private LocalDateTime emailSentAt;

    @Column(name = "email_status", length = 20)
    private String emailStatus; // PENDING, SENT, FAILED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isRead == null) {
            isRead = false;
        }
        if (emailSent == null) {
            emailSent = false;
        }
        if (emailStatus == null) {
            emailStatus = "PENDING";
        }
    }
}