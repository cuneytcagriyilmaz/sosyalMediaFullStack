package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Müşteri güncelleme isteği (Sadece gönderilen alanlar güncellenir)")
public class CustomerUpdateRequest {

    @Schema(description = "Şirket/Dükkan adı", example = "Cafe Sunshine")
    private String companyName;

    @Schema(description = "Sektör", example = "cafe")
    private String sector;

    @Schema(description = "Adres", example = "Yeni Adres, Antalya")
    private String address;

    @Schema(description = "Üyelik paketi", example = "Platinum")
    private String membershipPackage;

    @Schema(description = "Müşteri durumu", example = "ACTIVE")
    private Customer.CustomerStatus status;

    @Schema(description = "Özel günler için post yapılsın mı")
    private Boolean specialDates;

    @Schema(description = "Hedef bölge")
    private String targetRegion;

    @Schema(description = "Hashtagler")
    private String customerHashtags;

    @Schema(description = "Post türü")
    private String postType;

    @Schema(description = "Post sıklığı")
    private String postFrequency;

    @Schema(description = "Post tonu")
    private String postTone;

    @Schema(description = "Hedef yaş")
    private String audienceAge;

    @Schema(description = "Hedef ilgi alanları")
    private String audienceInterests;

    @Schema(description = "Birinci yetkili adı")
    private String customerContact1Name;

    @Schema(description = "Birinci yetkili soyadı")
    private String customerContact1Surname;

    @Schema(description = "Birinci yetkili email")
    private String customerContact1Email;

    @Schema(description = "Birinci yetkili telefon")
    private String customerContact1Phone;

    @Schema(description = "İkinci yetkili adı")
    private String customerContact2Name;

    @Schema(description = "İkinci yetkili soyadı")
    private String customerContact2Surname;

    @Schema(description = "İkinci yetkili email")
    private String customerContact2Email;

    @Schema(description = "İkinci yetkili telefon")
    private String customerContact2Phone;

    @Schema(description = "Üçüncü yetkili adı")
    private String customerContact3Name;

    @Schema(description = "Üçüncü yetkili soyadı")
    private String customerContact3Surname;

    @Schema(description = "Üçüncü yetkili email")
    private String customerContact3Email;

    @Schema(description = "Üçüncü yetkili telefon")
    private String customerContact3Phone;

    @Schema(description = "Instagram")
    private String instagram;

    @Schema(description = "Facebook")
    private String facebook;

    @Schema(description = "TikTok")
    private String tiktok;

    @Schema(description = "Google Console Email")
    private String googleConsoleEmail;

    @Schema(description = "SEO başlık")
    private String seoTitleSuggestions;

    @Schema(description = "SEO içerik")
    private String seoContentSuggestions;

    @Schema(description = "Instagram API Key")
    private String instagramApiKey;

    @Schema(description = "Facebook API Key")
    private String facebookApiKey;

    @Schema(description = "TikTok API Key")
    private String tiktokApiKey;

    @Schema(description = "Google API Key")
    private String googleApiKey;


}