package com.sosyalmedia.customerservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(length = 200)
    private String originalFileName;

    @Column
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public enum MediaType {
        LOGO,
        PHOTO,
        VIDEO,
        DOCUMENT
    }
}