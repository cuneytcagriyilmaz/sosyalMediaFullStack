package com.sosyalmedia.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "post_deadlines")
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

    //Post ile ilişkilendirme (opsiyonel, şimdilik null olabilir)
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

    @Enumerated(EnumType.STRING) // ✅ YENİ: Enum olarak
    @Column(length = 50)
    private Platform platform; // ✅ DEĞİŞTİ: String -> Platform

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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

    public enum PostDeadlineStatus {
        NOT_STARTED("Başlanmadı", "#94A3B8"),
        IN_PROGRESS("Hazırlanıyor", "#F59E0B"),
        READY("Hazırlandı", "#3B82F6"),
        SENT("Gönderildi", "#10B981");

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
}