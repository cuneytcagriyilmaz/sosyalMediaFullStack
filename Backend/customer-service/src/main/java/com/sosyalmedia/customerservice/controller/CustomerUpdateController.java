// src/main/java/com/sosyalmedia/customerservice/controller/CustomerUpdateController.java

package com.sosyalmedia.customerservice.controller;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.dto.request.BasicInfoUpdateRequest;
import com.sosyalmedia.customerservice.dto.response.ApiResponse;
import com.sosyalmedia.customerservice.dto.response.CustomerResponse;
import com.sosyalmedia.customerservice.service.CustomerUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/update")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Update", description = "Müşteri Bölüm Bazlı Güncelleme API'leri")
public class CustomerUpdateController {

    private final CustomerUpdateService customerUpdateService;

    @Operation(
            summary = "Temel bilgileri güncelle",
            description = "Şirket adı, sektör, adres, üyelik paketi ve durum bilgilerini günceller"
    )
    @PutMapping("/basic-info")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateBasicInfo(
            @Parameter(description = "Müşteri ID", required = true, example = "1")
            @PathVariable Long customerId,
            @Valid @RequestBody BasicInfoUpdateRequest request) {

        log.info("REST: Updating basic info for customer ID: {}", customerId);
        CustomerResponse response = customerUpdateService.updateBasicInfo(customerId, request);

        return ResponseEntity.ok(
                ApiResponse.success("✅ Temel bilgiler başarıyla güncellendi", response)
        );
    }

    @Operation(
            summary = "İletişim kişilerini güncelle",
            description = "Tüm contact listesini yeniler. Duplicate email/telefon kontrolü yapar."
    )
    @PutMapping("/contacts")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateContacts(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long customerId,
            @Valid @RequestBody List<CustomerContactDTO> contacts) {

        log.info("REST: Updating contacts for customer ID: {}", customerId);
        CustomerResponse response = customerUpdateService.updateContacts(customerId, contacts);

        return ResponseEntity.ok(
                ApiResponse.success("✅ İletişim kişileri başarıyla güncellendi", response)
        );
    }

    @Operation(
            summary = "Sosyal medya hesaplarını güncelle",
            description = "Instagram, Facebook ve TikTok hesap bilgilerini günceller"
    )
    @PutMapping("/social-media")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateSocialMedia(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerSocialMediaDTO socialMedia) {

        log.info("REST: Updating social media for customer ID: {}", customerId);
        CustomerResponse response = customerUpdateService.updateSocialMedia(customerId, socialMedia);

        return ResponseEntity.ok(
                ApiResponse.success("✅ Sosyal medya bilgileri başarıyla güncellendi", response)
        );
    }

    @Operation(
            summary = "Hedef kitle ve içerik stratejisini güncelle",
            description = "Post türü, sıklığı, tonu, hedef bölge, yaş aralığı ve ilgi alanlarını günceller"
    )
    @PutMapping("/target-audience")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateTargetAudience(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerTargetAudienceDTO targetAudience) {

        log.info("REST: Updating target audience for customer ID: {}", customerId);
        CustomerResponse response = customerUpdateService.updateTargetAudience(customerId, targetAudience);

        return ResponseEntity.ok(
                ApiResponse.success("✅ Hedef kitle bilgileri başarıyla güncellendi", response)
        );
    }

    @Operation(
            summary = "SEO bilgilerini güncelle",
            description = "Google Console email, SEO başlık ve içerik önerilerini günceller"
    )
    @PutMapping("/seo")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateSeo(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerSeoDTO seo) {

        log.info("REST: Updating SEO for customer ID: {}", customerId);
        CustomerResponse response = customerUpdateService.updateSeo(customerId, seo);

        return ResponseEntity.ok(
                ApiResponse.success("✅ SEO bilgileri başarıyla güncellendi", response)
        );
    }

    @Operation(
            summary = "API anahtarlarını güncelle",
            description = "Instagram, Facebook, TikTok ve Google API anahtarlarını günceller"
    )
    @PutMapping("/api-keys")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateApiKeys(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerApiKeyDTO apiKeys) {

        log.info("REST: Updating API keys for customer ID: {}", customerId);
        CustomerResponse response = customerUpdateService.updateApiKeys(customerId, apiKeys);

        return ResponseEntity.ok(
                ApiResponse.success("✅ API anahtarları başarıyla güncellendi", response)
        );
    }
}
