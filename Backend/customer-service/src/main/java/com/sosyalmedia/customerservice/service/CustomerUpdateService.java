
package com.sosyalmedia.customerservice.service;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.dto.request.BasicInfoUpdateRequest;
import com.sosyalmedia.customerservice.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerUpdateService {

    /**
     * Temel bilgileri günceller (Şirket adı, sektör, adres, paket, durum)
     */
    CustomerResponse updateBasicInfo(Long customerId, BasicInfoUpdateRequest request);

    /**
     * İletişim kişilerini günceller (ESKİ contact'ları siler, YENİ'leri ekler)
     * Duplicate kontrol yapar (email, telefon)
     */
    CustomerResponse updateContacts(Long customerId, List<CustomerContactDTO> contacts);

    /**
     * Sosyal medya hesaplarını günceller
     */
    CustomerResponse updateSocialMedia(Long customerId, CustomerSocialMediaDTO socialMedia);

    /**
     * Hedef kitle ve içerik stratejisini günceller
     */
    CustomerResponse updateTargetAudience(Long customerId, CustomerTargetAudienceDTO targetAudience);

    /**
     * SEO bilgilerini günceller
     */
    CustomerResponse updateSeo(Long customerId, CustomerSeoDTO seo);

    /**
     * API anahtarlarını günceller
     */
    CustomerResponse updateApiKeys(Long customerId, CustomerApiKeyDTO apiKeys);
}