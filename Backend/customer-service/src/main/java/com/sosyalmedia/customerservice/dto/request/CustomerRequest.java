package com.sosyalmedia.customerservice.dto.request;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    // ✅ NESTED DTO'LAR
    @Schema(description = "Hedef kitle ve içerik tercihleri (Zorunlu)")
    @NotNull(message = "Hedef kitle bilgileri gerekli")
    @Valid
    private CustomerTargetAudienceDTO targetAudience;

    @Schema(description = "İletişim bilgileri listesi (En az 1 gerekli)")
    @NotEmpty(message = "En az bir yetkili gerekli")
    @Valid
    @Builder.Default
    private List<CustomerContactDTO> contacts = new ArrayList<>();

    @Schema(description = "Sosyal medya hesapları (Opsiyonel)")
    @Valid
    private CustomerSocialMediaDTO socialMedia;

    @Schema(description = "SEO bilgileri (Opsiyonel)")
    @Valid
    private CustomerSeoDTO seo;

    @Schema(description = "API anahtarları (Opsiyonel)")
    @Valid
    private CustomerApiKeyDTO apiKeys;
}