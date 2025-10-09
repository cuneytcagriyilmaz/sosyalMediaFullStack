package com.sosyalmedia.customerservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_social_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String instagram;

    @Column(length = 100)
    private String facebook;

    @Column(length = 100)
    private String tiktok;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
}