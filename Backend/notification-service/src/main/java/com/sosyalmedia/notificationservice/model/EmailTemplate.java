package com.sosyalmedia.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "email_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_key", unique = true, nullable = false)
    private String templateKey;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "subject_template", nullable = false)
    private String subjectTemplate;

    @Column(name = "body_template", columnDefinition = "TEXT", nullable = false)
    private String bodyTemplate;

    @Column(name = "template_type")
    private String templateType; // NOTIFICATION, REMINDER, ALERT

    @ElementCollection // ✅ List<String> için gerekli
    @CollectionTable(name = "email_template_variables", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "variable_name")
    private List<String> variables;

    @Column(name = "is_active")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}