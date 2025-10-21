// src/main/java/com/sosyalmedia/analytics/model/ActivityLog.java

package com.sosyalmedia.analytics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 50)
    private ActivityType activityType;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "icon", length = 10)
    private String icon;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Enum
    public enum ActivityType {
        POST_SENT,
        POST_READY,
        AI_COMPLETED,
        DEADLINE_APPROACHING,
        NEW_CUSTOMER,
        CUSTOMER_UPDATED,
        CONTENT_APPROVED,
        MEDIA_UPLOADED
    }
}