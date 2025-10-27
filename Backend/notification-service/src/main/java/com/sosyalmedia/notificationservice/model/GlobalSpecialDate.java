package com.sosyalmedia.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "global_special_dates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSpecialDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_type", nullable = false, length = 50)
    private String dateType; // NATIONAL_HOLIDAY, RELIGIOUS, SPECIAL_DAY

    @Column(name = "date_name", nullable = false, length = 200)
    private String dateName;

    @Column(name = "date_value", nullable = false)
    private LocalDate dateValue;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon", length = 10)
    private String icon;

    @Column(name = "is_recurring")
    private Boolean isRecurring; // Her yıl tekrar eder mi

    @Column(name = "auto_suggest_post")
    private Boolean autoSuggestPost; // Post önerisi yapılacak mı

    @Column(name = "applicable_sectors", columnDefinition = "TEXT")
    private String applicableSectors; // JSON format: ["cafe", "restaurant"]

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isRecurring == null) {
            isRecurring = true;
        }
        if (autoSuggestPost == null) {
            autoSuggestPost = true;
        }
    }
}
