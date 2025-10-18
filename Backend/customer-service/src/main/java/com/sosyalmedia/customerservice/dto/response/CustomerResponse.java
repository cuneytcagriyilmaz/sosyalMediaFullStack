package com.sosyalmedia.customerservice.dto.response;

import com.sosyalmedia.customerservice.dto.*;
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

    @Schema(description = "Hedef kitle ve içerik tercihleri")
    private CustomerTargetAudienceDTO targetAudience;

    @Schema(description = "İletişim bilgileri listesi")
    @Builder.Default
    private List<CustomerContactDTO> contacts = new ArrayList<>();

    @Schema(description = "Sosyal medya hesapları")
    private CustomerSocialMediaDTO socialMedia;

    @Schema(description = "SEO bilgileri")
    private CustomerSeoDTO seo;

    @Schema(description = "API anahtarları")
    private CustomerApiKeyDTO apiKeys;

    @Schema(description = "Medya dosyaları (logo, fotoğraf, video, döküman)")
    @Builder.Default
    private List<CustomerMediaDTO> media = new ArrayList<>();

    @Schema(description = "Oluşturulma tarihi")
    private LocalDateTime createdAt;

    @Schema(description = "Güncellenme tarihi")
    private LocalDateTime updatedAt;

    @Schema(description = "Silinme tarihi (Soft delete için)")
    private LocalDateTime deletedAt;
}