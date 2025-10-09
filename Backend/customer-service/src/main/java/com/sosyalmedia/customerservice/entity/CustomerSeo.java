package com.sosyalmedia.customerservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_seo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSeo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String googleConsoleEmail;

    @Column(length = 2000)
    private String seoTitleSuggestions;

    @Column(length = 5000)
    private String seoContentSuggestions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
}