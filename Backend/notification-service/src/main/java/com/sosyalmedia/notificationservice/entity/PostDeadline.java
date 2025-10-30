package com.sosyalmedia.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "post_deadlines", indexes = {
        @Index(name = "idx_customer_id", columnList = "customer_id"),
        @Index(name = "idx_scheduled_date", columnList = "scheduled_date"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_customer_date", columnList = "customer_id, scheduled_date"),
        @Index(name = "idx_event_type", columnList = "event_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDeadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PostDeadlineStatus status = PostDeadlineStatus.NOT_STARTED;

    @Column(name = "content_ready", nullable = false)
    @Builder.Default
    private Boolean contentReady = false;

    @Column(name = "post_content", length = 1000)
    private String postContent;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Platform platform;

    // ========== ✅ YENİ COLUMN'LAR (Auto Schedule) ==========

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", length = 20)
    @Builder.Default
    private EventType eventType = EventType.REGULAR;

    @Column(name = "auto_created", nullable = false)
    @Builder.Default
    private Boolean autoCreated = false;

    @Column(name = "holiday_name", length = 100)
    private String holidayName;

    @Column(name = "holiday_type", length = 50)
    private String holidayType;

    // ========== ✅ YENİ COLUMN'LAR (Notification) ==========

    @Column(name = "notification_sent", nullable = false)
    @Builder.Default
    private Boolean notificationSent = false;

    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;

    // ========== AUDIT ==========

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ========== TRANSIENT METHODS ==========

    @Transient
    public int getDaysRemaining() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), scheduledDate);
    }

    @Transient
    public UrgencyLevel getUrgencyLevel() {
        int daysRemaining = getDaysRemaining();

        if (daysRemaining < 0) {
            return UrgencyLevel.OVERDUE;
        } else if (daysRemaining <= 1) {
            return UrgencyLevel.CRITICAL;
        } else if (daysRemaining <= 3) {
            return UrgencyLevel.WARNING;
        } else if (daysRemaining <= 7) {
            return UrgencyLevel.NORMAL;
        } else {
            return UrgencyLevel.DISTANT;
        }
    }

    // ========== ENUM'LAR ==========

    public enum PostDeadlineStatus {
        NOT_STARTED("Başlanmadı", "#94A3B8"),
        IN_PROGRESS("Hazırlanıyor", "#F59E0B"),
        READY("Hazırlandı", "#3B82F6"),
        SENT("Gönderildi", "#10B981"),
        CANCELLED("İptal Edildi", "#DC2626");  // ✅ YENİ

        private final String displayName;
        private final String colorCode;

        PostDeadlineStatus(String displayName, String colorCode) {
            this.displayName = displayName;
            this.colorCode = colorCode;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getColorCode() {
            return colorCode;
        }
    }

    public enum UrgencyLevel {
        OVERDUE(-999, -1, "#DC2626", "Gecikmiş"),
        CRITICAL(0, 1, "#EF4444", "Kritik"),
        WARNING(2, 3, "#F59E0B", "Uyarı"),
        NORMAL(4, 7, "#10B981", "Normal"),
        DISTANT(8, 999, "#94A3B8", "Uzak");

        private final int minDays;
        private final int maxDays;
        private final String colorCode;
        private final String displayName;

        UrgencyLevel(int minDays, int maxDays, String colorCode, String displayName) {
            this.minDays = minDays;
            this.maxDays = maxDays;
            this.colorCode = colorCode;
            this.displayName = displayName;
        }

        public int getMinDays() {
            return minDays;
        }

        public int getMaxDays() {
            return maxDays;
        }

        public String getColorCode() {
            return colorCode;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum EventType {
        FIRST_POST("İlk Post", "#9333EA"),       // Mor
        REGULAR("Düzenli Post", "#3B82F6"),      // Mavi
        SPECIAL_DATE("Özel Gün", "#F59E0B");     // Turuncu

        private final String displayName;
        private final String colorCode;

        EventType(String displayName, String colorCode) {
            this.displayName = displayName;
            this.colorCode = colorCode;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getColorCode() {
            return colorCode;
        }
    }
}