package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    private Customer.CustomerStatus status = Customer.CustomerStatus.ACTIVE;

    @Schema(description = "Özel günler için post yapılsın mı", example = "true", defaultValue = "false")
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

    @Schema(description = "Birinci yetkili adı", example = "Ahmet")
    @NotBlank(message = "En az bir yetkili adı gerekli")
    private String customerContact1Name;

    @Schema(description = "Birinci yetkili soyadı", example = "Yılmaz")
    @NotBlank(message = "En az bir yetkili soyadı gerekli")
    private String customerContact1Surname;

    @Schema(description = "Birinci yetkili email", example = "ahmet@sunshine.com")
    @NotBlank(message = "En az bir yetkili email gerekli")
    private String customerContact1Email;

    @Schema(description = "Birinci yetkili telefon", example = "5551234567")
    @NotBlank(message = "En az bir yetkili telefon gerekli")
    private String customerContact1Phone;

    @Schema(description = "İkinci yetkili adı", example = "Ayşe")
    private String customerContact2Name;

    @Schema(description = "İkinci yetkili soyadı", example = "Kaya")
    private String customerContact2Surname;

    @Schema(description = "İkinci yetkili email", example = "ayse@sunshine.com")
    private String customerContact2Email;

    @Schema(description = "İkinci yetkili telefon", example = "5552345678")
    private String customerContact2Phone;

    @Schema(description = "Üçüncü yetkili adı")
    private String customerContact3Name;

    @Schema(description = "Üçüncü yetkili soyadı")
    private String customerContact3Surname;

    @Schema(description = "Üçüncü yetkili email")
    private String customerContact3Email;

    @Schema(description = "Üçüncü yetkili telefon")
    private String customerContact3Phone;

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

    // DOSYA ALANLARI KALDIRILDI
    // customerLogos ve customerPhotos yok artık
}