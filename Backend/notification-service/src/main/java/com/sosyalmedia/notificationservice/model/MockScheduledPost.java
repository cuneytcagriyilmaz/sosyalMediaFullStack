package com.sosyalmedia.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mock_scheduled_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockScheduledPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "post_type")
    private String postType; // NORMAL, SPECIAL_DAY

    @Column(name = "is_special_day_post")
    private Boolean isSpecialDayPost;

    @Column(name = "special_date_id")
    private Long specialDateId;

    @Column(name = "status")
    private String status; // SCHEDULED, READY, PUBLISHED

    @Column(name = "preparation_status")
    private String preparationStatus; // NOT_STARTED, IN_PROGRESS, READY, SENT

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "platforms")
    private String platforms; // Instagram,Facebook,Twitter

    @Column(name = "notes", columnDefinition = "TEXT") // ✅ EKLE
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at") // ✅ EKLE
    private LocalDateTime publishedAt;
}