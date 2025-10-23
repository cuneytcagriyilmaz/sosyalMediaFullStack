// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/entity/ActivityLog.java

package com.sosyalmedia.analytics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "icon")
    private String icon;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum ActivityType {
        POST_SENT,              // Post gönderildi
        POST_READY,             // Post hazır
        AI_COMPLETED,           // AI tamamlandı
        NEW_CUSTOMER,           // Yeni müşteri
        CUSTOMER_UPDATED,       // Müşteri güncellendi
        CONTENT_APPROVED,       // İçerik onaylandı
        MEDIA_UPLOADED,         // Medya yüklendi
        DEADLINE_APPROACHING,   // Deadline yaklaşıyor
        TASK_COMPLETED,         // Görev tamamlandı
        ONBOARDING_COMPLETED    // Onboarding tamamlandı
    }
}