package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

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

    private String customerContact1Name;
    private String customerContact1Surname;
    private String customerContact1Email;
    private String customerContact1Phone;

    private String customerContact2Name;
    private String customerContact2Surname;
    private String customerContact2Email;
    private String customerContact2Phone;

    private String customerContact3Name;
    private String customerContact3Surname;
    private String customerContact3Email;
    private String customerContact3Phone;

    private String instagram;
    private String facebook;
    private String tiktok;

    private String googleConsoleEmail;
    private String seoTitleSuggestions;
    private String seoContentSuggestions;

    private String instagramApiKey;
    private String facebookApiKey;
    private String tiktokApiKey;
    private String googleApiKey;

    @Schema(description = "Oluşturulma tarihi")
    private LocalDateTime createdAt;

    @Schema(description = "Güncellenme tarihi")
    private LocalDateTime updatedAt;

    // DOSYA ALANLARI KALDIRILDI
    // customerLogos ve customerPhotos yok artık
    // Dosyaları görmek için GET /api/customers/{id}/files kullanılacak
}