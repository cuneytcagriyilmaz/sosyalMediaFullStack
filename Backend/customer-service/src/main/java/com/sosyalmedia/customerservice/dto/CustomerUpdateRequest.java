package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Müşteri güncelleme isteği")
public class CustomerUpdateRequest {

    @Schema(description = "Şirket/Dükkan adı")
    private String companyName;

    @Schema(description = "Sektör")
    private String sector;

    @Schema(description = "Adres")
    private String address;

    @Schema(description = "Üyelik paketi")
    private String membershipPackage;

    @Schema(description = "Müşteri durumu")
    private Customer.CustomerStatus status;

    @Schema(description = "Özel günler")
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

    // YENİ: Contact listesi
    @Schema(description = "İletişim bilgileri listesi (gönderilirse tamamen değiştirilir)")
    @Valid
    private List<ContactDTO> contacts;

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