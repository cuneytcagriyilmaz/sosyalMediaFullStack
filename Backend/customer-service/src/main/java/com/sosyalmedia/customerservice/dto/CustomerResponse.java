package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Müşteri detay bilgileri")
public class CustomerResponse {

    @Schema(description = "Müşteri ID", example = "1")
    private Long id;

    @Schema(description = "Şirket adı", example = "Cafe Sunshine")
    private String companyName;

    @Schema(description = "Sektör", example = "cafe")
    private String sector;

    @Schema(description = "Adres", example = "Lara Plajı, Antalya")
    private String address;

    @Schema(description = "Üyelik paketi", example = "Gold")
    private String membershipPackage;

    @Schema(description = "Durum", example = "ACTIVE")
    private Customer.CustomerStatus status;

    @Schema(description = "Özel günler aktif mi", example = "true")
    private Boolean specialDates;

    @Schema(description = "Hedef bölge", example = "Antalya")
    private String targetRegion;

    @Schema(description = "Hashtagler", example = "#kahve #antalya")
    private String customerHashtags;

    @Schema(description = "Post türü", example = "gorsel")
    private String postType;

    @Schema(description = "Post sıklığı", example = "2")
    private String postFrequency;

    @Schema(description = "Post tonu", example = "samimi")
    private String postTone;

    @Schema(description = "Hedef yaş", example = "20-45")
    private String audienceAge;

    @Schema(description = "Hedef ilgi alanları", example = "Kahve, Deniz")
    private String audienceInterests;

    // YENİ: Contact listesi
    @Schema(description = "İletişim bilgileri listesi")
    @Builder.Default
    private List<ContactDTO> contacts = new ArrayList<>();

    @Schema(description = "Instagram")
    private String instagram;

    @Schema(description = "Facebook")
    private String facebook;

    @Schema(description = "TikTok")
    private String tiktok;

    @Schema(description = "Google Console email")
    private String googleConsoleEmail;

    @Schema(description = "SEO başlık önerileri")
    private String seoTitleSuggestions;

    @Schema(description = "SEO içerik önerileri")
    private String seoContentSuggestions;

    @Schema(description = "Instagram API Key")
    private String instagramApiKey;

    @Schema(description = "Facebook API Key")
    private String facebookApiKey;

    @Schema(description = "TikTok API Key")
    private String tiktokApiKey;

    @Schema(description = "Google API Key")
    private String googleApiKey;

    @Schema(description = "Oluşturulma tarihi")
    private LocalDateTime createdAt;

    @Schema(description = "Güncellenme tarihi")
    private LocalDateTime updatedAt;
}