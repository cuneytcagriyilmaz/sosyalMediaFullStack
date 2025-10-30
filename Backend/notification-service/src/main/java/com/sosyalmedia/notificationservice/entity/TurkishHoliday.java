package com.sosyalmedia.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "turkish_holidays", indexes = {
        @Index(name = "idx_year", columnList = "year"),
        @Index(name = "idx_date", columnList = "holiday_date"),
        @Index(name = "idx_year_date", columnList = "year, holiday_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurkishHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "primary_type", length = 50)
    private String primaryType;  // "National holiday", "Observance"

    @CreationTimestamp
    @Column(name = "cached_at", updatable = false)
    private LocalDateTime cachedAt;
}