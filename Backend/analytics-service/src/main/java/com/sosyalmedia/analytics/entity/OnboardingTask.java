// src/main/java/com/sosyalmedia/analytics/model/OnboardingTask.java

package com.sosyalmedia.analytics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "onboarding_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "task_name", nullable = false, length = 255)
    private String taskName;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", length = 50)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", length = 50)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TaskStatus status;

    @Column(name = "connection_status")
    private Boolean connectionStatus = false;

    @Column(name = "connection_date")
    private LocalDateTime connectionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum TaskType {
        SOCIAL_MEDIA_CONNECT,
        FIRST_POST,
        SETTINGS,
        PAYMENT
    }

    public enum Platform {
        INSTAGRAM,
        TIKTOK,
        FACEBOOK,
        YOUTUBE
    }

    public enum TaskStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}