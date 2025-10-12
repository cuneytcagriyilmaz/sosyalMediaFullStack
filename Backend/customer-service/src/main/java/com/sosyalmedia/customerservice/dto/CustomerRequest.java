package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Yeni müşteri oluşturma isteği")
public class CustomerRequest {

    @Schema(description = "Şirket/Dükkan adı", example = "Cafe Sunshine")
    @NotBlank(message = "Şirket adı boş olamaz")
    private String companyName;

    @Schema(description = "Sektör", example = "cafe")
    @NotBlank(message = "Sektör boş olamaz")
    private String sector;

    @Schema(description = "Adres", example = "Lara Plajı, Antalya")
    @NotBlank(message = "Adres boş olamaz")
    private String address;

    @Schema(description = "Üyelik paketi", example = "Gold")
    @NotBlank(message = "Üyelik paketi boş olamaz")
    private String membershipPackage;

    @Schema(description = "Müşteri durumu", example = "ACTIVE", defaultValue = "ACTIVE")
    @Builder.Default
    private Customer.CustomerStatus status = Customer.CustomerStatus.ACTIVE;

    @Schema(description = "Özel günler için post yapılsın mı", example = "true", defaultValue = "false")
    @Builder.Default
    private Boolean specialDates = false;

    @Schema(description = "Hedef bölge", example = "Antalya, Lara")
    private String targetRegion;

    @Schema(description = "Müşterinin önerdiği hashtagler", example = "#kahve #antalya #cafe")
    private String customerHashtags;

    @Schema(description = "Post türü", example = "gorsel", allowableValues = {"gorsel", "video", "story", "genel"})
    @NotBlank(message = "Post türü boş olamaz")
    private String postType;

    @Schema(description = "Haftalık post sıklığı", example = "2")
    @NotBlank(message = "Post sıklığı boş olamaz")
    private String postFrequency;

    @Schema(description = "Post tonu", example = "samimi", allowableValues = {"samimi", "resmi", "mizahi", "ciddi"})
    @NotBlank(message = "Post tonu boş olamaz")
    private String postTone;

    @Schema(description = "Hedef kitle yaş aralığı", example = "20-45")
    private String audienceAge;

    @Schema(description = "Hedef kitle ilgi alanları", example = "Kahve, Deniz, Fotograf")
    private String audienceInterests;

    // YENİ: Contact listesi
    @Schema(description = "İletişim bilgileri listesi (En az 1 gerekli)")
    @NotEmpty(message = "En az bir yetkili gerekli")
    @Valid
    @Builder.Default
    private List<ContactDTO> contacts = new ArrayList<>();

    @Schema(description = "Instagram kullanıcı adı", example = "sunshine_cafe")
    private String instagram;

    @Schema(description = "Facebook sayfa adı", example = "SunshineCafe")
    private String facebook;

    @Schema(description = "TikTok kullanıcı adı", example = "sunshine_tt")
    private String tiktok;

    @Schema(description = "Google Console email adresi")
    private String googleConsoleEmail;

    @Schema(description = "SEO başlık önerileri")
    private String seoTitleSuggestions;

    @Schema(description = "SEO içerik önerileri")
    private String seoContentSuggestions;

    @Schema(description = "Instagram API anahtarı")
    private String instagramApiKey;

    @Schema(description = "Facebook API anahtarı")
    private String facebookApiKey;

    @Schema(description = "TikTok API anahtarı")
    private String tiktokApiKey;

    @Schema(description = "Google API anahtarı")
    private String googleApiKey;
}