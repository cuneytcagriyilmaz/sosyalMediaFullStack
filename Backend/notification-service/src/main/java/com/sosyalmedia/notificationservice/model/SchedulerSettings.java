package com.sosyalmedia.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduler_settings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_key", unique = true, nullable = false, length = 100)
    private String settingKey;

    @Column(name = "setting_name", nullable = false, length = 200)
    private String settingName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cron_expression", nullable = false, length = 100)
    private String cronExpression;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_executed_at") // ✅ EKLE
    private LocalDateTime lastExecutedAt;

    @Column(name = "next_execution_at") // ✅ EKLE
    private LocalDateTime nextExecutionAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}