package com.sosyalmedia.customerservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_api_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String instagramApiKey;

    @Column(length = 500)
    private String facebookApiKey;

    @Column(length = 500)
    private String tiktokApiKey;

    @Column(length = 500)
    private String googleApiKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
}