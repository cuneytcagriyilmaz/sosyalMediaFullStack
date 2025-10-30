package com.sosyalmedia.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_deadlines_archive")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDeadlineArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_deadline_id")
    private Long originalDeadlineId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostDeadline.PostDeadlineStatus finalStatus;

    @Column(name = "content_ready")
    private Boolean contentReady;

    @Column(name = "post_content", length = 1000)
    private String postContent;

    @Enumerated(EnumType.STRING) //Enum olarak
    @Column(length = 50)
    private Platform platform; //  String -> Platform

    @Column(name = "archived_reason", length = 50)
    private String archivedReason;

    @CreationTimestamp
    @Column(name = "archived_at", nullable = false, updatable = false)
    private LocalDateTime archivedAt;

    @Column(name = "original_created_at")
    private LocalDateTime originalCreatedAt;

    @Column(name = "original_updated_at")
    private LocalDateTime originalUpdatedAt;
}