package com.sosyalmedia.customerservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_target_audience")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerTargetAudience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean specialDates = false;

    @Column(length = 200)
    private String targetRegion;

    // Virgülle ayrılmış taglar: "#kahve, #antalya, #cafe"
    @Column(length = 1000)
    private String customerHashtags;

    @Column(nullable = false, length = 50)
    private String postType;

    @Column(nullable = false, length = 50)
    private String postFrequency;

    @Column(nullable = false, length = 50)
    private String postTone;

    @Column(length = 50)
    private String audienceAge;

    @Column(length = 500)
    private String audienceInterests;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
}