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
@Schema(description = "Müşteri güncelleme isteği - Tüm alanlar opsiyonel, sadece gönderilen alanlar güncellenir")
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

    // ✅ NESTED DTO'LAR (Tümü opsiyonel - null ise güncellenmez)
    @Schema(description = "Hedef kitle ve içerik tercihleri (Gönderilirse tamamen değiştirilir)")
    @Valid
    private CustomerTargetAudienceDTO targetAudience;

    @Schema(description = "İletişim bilgileri listesi (Gönderilirse tamamen değiştirilir)")
    @Valid
    private List<CustomerContactDTO> contacts;

    @Schema(description = "Sosyal medya hesapları (Gönderilirse tamamen değiştirilir)")
    @Valid
    private CustomerSocialMediaDTO socialMedia;

    @Schema(description = "SEO bilgileri (Gönderilirse tamamen değiştirilir)")
    @Valid
    private CustomerSeoDTO seo;

    @Schema(description = "API anahtarları (Gönderilirse tamamen değiştirilir)")
    @Valid
    private CustomerApiKeyDTO apiKeys;
}