package com.sosyalmedia.customerservice.mapper;

import com.sosyalmedia.customerservice.config.FileUploadProperties;
import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.dto.request.CustomerRequest;
import com.sosyalmedia.customerservice.dto.response.CustomerListResponse;
import com.sosyalmedia.customerservice.dto.response.CustomerResponse;
import com.sosyalmedia.customerservice.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor // ✅ Lombok ile constructor injection
public class CustomerMapper {

    private final FileUploadProperties fileUploadProperties; // ✅ Config'den inject et

    // ========== CREATE MAPPING ==========

    // CustomerRequest → Entity (CREATE için)
    public Customer toEntity(CustomerRequest request) {
        Customer customer = Customer.builder()
                .companyName(request.getCompanyName())
                .sector(request.getSector())
                .address(request.getAddress())
                .membershipPackage(request.getMembershipPackage())
                .status(request.getStatus())
                .build();

        // Target Audience - Nested DTO'dan map et
        if (request.getTargetAudience() != null) {
            CustomerTargetAudience targetAudience = CustomerTargetAudience.builder()
                    .specialDates(request.getTargetAudience().getSpecialDates())
                    .targetRegion(request.getTargetAudience().getTargetRegion())
                    .customerHashtags(request.getTargetAudience().getCustomerHashtags())
                    .postType(request.getTargetAudience().getPostType())
                    .postFrequency(request.getTargetAudience().getPostFrequency())
                    .postTone(request.getTargetAudience().getPostTone())
                    .audienceAge(request.getTargetAudience().getAudienceAge())
                    .audienceInterests(request.getTargetAudience().getAudienceInterests())
                    .customer(customer)
                    .build();
            customer.setTargetAudience(targetAudience);
        }

        // Contacts - Liste olarak
        if (request.getContacts() != null && !request.getContacts().isEmpty()) {
            for (int i = 0; i < request.getContacts().size(); i++) {
                CustomerContactDTO dto = request.getContacts().get(i);
                CustomerContact contact = CustomerContact.builder()
                        .name(dto.getName())
                        .surname(dto.getSurname())
                        .email(dto.getEmail())
                        .phone(dto.getPhone())
                        .priority(dto.getPriority() != null ? dto.getPriority() : i + 1)
                        .customer(customer)
                        .build();
                customer.getContacts().add(contact);
            }
        }

        // Social Media - Nested DTO'dan map et
        if (request.getSocialMedia() != null && hasSocialMediaContent(request.getSocialMedia())) {
            CustomerSocialMedia socialMedia = CustomerSocialMedia.builder()
                    .instagram(request.getSocialMedia().getInstagram())
                    .facebook(request.getSocialMedia().getFacebook())
                    .tiktok(request.getSocialMedia().getTiktok())
                    .customer(customer)
                    .build();
            customer.setSocialMedia(socialMedia);
        }

        // SEO - Nested DTO'dan map et
        if (request.getSeo() != null && hasSeoContent(request.getSeo())) {
            CustomerSeo seo = CustomerSeo.builder()
                    .googleConsoleEmail(request.getSeo().getGoogleConsoleEmail())
                    .seoTitleSuggestions(request.getSeo().getTitleSuggestions())
                    .seoContentSuggestions(request.getSeo().getContentSuggestions())
                    .customer(customer)
                    .build();
            customer.setSeo(seo);
        }

        // API Keys - Nested DTO'dan map et
        if (request.getApiKeys() != null && hasApiKeysContent(request.getApiKeys())) {
            CustomerApiKey apiKey = CustomerApiKey.builder()
                    .instagramApiKey(request.getApiKeys().getInstagramApiKey())
                    .facebookApiKey(request.getApiKeys().getFacebookApiKey())
                    .tiktokApiKey(request.getApiKeys().getTiktokApiKey())
                    .googleApiKey(request.getApiKeys().getGoogleApiKey())
                    .customer(customer)
                    .build();
            customer.setApiKey(apiKey);
        }

        return customer;
    }

    // ========== RESPONSE MAPPING ==========

    // Entity → CustomerResponse
    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse.CustomerResponseBuilder builder = CustomerResponse.builder()
                .id(customer.getId())
                .companyName(customer.getCompanyName())
                .sector(customer.getSector())
                .address(customer.getAddress())
                .membershipPackage(customer.getMembershipPackage())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .deletedAt(customer.getDeletedAt());

        // Target Audience - Nested DTO olarak
        if (customer.getTargetAudience() != null) {
            builder.targetAudience(toTargetAudienceDTO(customer.getTargetAudience()));
        }

        // Contacts - Priority'ye göre sıralı
        if (customer.getContacts() != null && !customer.getContacts().isEmpty()) {
            builder.contacts(customer.getContacts().stream()
                    .sorted(Comparator.comparing(CustomerContact::getPriority))
                    .map(this::toContactDTO)
                    .collect(Collectors.toList()));
        }

        // Social Media - Nested DTO olarak
        if (customer.getSocialMedia() != null) {
            builder.socialMedia(toSocialMediaDTO(customer.getSocialMedia()));
        }

        // SEO - Nested DTO olarak
        if (customer.getSeo() != null) {
            builder.seo(toSeoDTO(customer.getSeo()));
        }

        // API Keys - Nested DTO olarak
        if (customer.getApiKey() != null) {
            builder.apiKeys(toApiKeyDTO(customer.getApiKey()));
        }

        // ✅ MEDIA - Nested DTO olarak
        if (customer.getMedia() != null && !customer.getMedia().isEmpty()) {
            builder.media(customer.getMedia().stream()
                    .map(this::toMediaDTO)
                    .collect(Collectors.toList()));
        }

        return builder.build();
    }

    // Entity → CustomerListResponse
    public CustomerListResponse toListResponse(Customer customer) {
        return CustomerListResponse.builder()
                .id(customer.getId())
                .companyName(customer.getCompanyName())
                .sector(customer.getSector())
                .membershipPackage(customer.getMembershipPackage())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .build();
    }

    // ========== DTO CONVERTERS ==========

    // CustomerTargetAudience → CustomerTargetAudienceDTO
    public CustomerTargetAudienceDTO toTargetAudienceDTO(CustomerTargetAudience entity) {
        return CustomerTargetAudienceDTO.builder()
                .id(entity.getId())
                .specialDates(entity.getSpecialDates())
                .targetRegion(entity.getTargetRegion())
                .customerHashtags(entity.getCustomerHashtags())
                .postType(entity.getPostType())
                .postFrequency(entity.getPostFrequency())
                .postTone(entity.getPostTone())
                .audienceAge(entity.getAudienceAge())
                .audienceInterests(entity.getAudienceInterests())
                .build();
    }

    // CustomerContact → CustomerContactDTO
    public CustomerContactDTO toContactDTO(CustomerContact entity) {
        return CustomerContactDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .priority(entity.getPriority())
                .build();
    }

    // CustomerSocialMedia → CustomerSocialMediaDTO
    public CustomerSocialMediaDTO toSocialMediaDTO(CustomerSocialMedia entity) {
        return CustomerSocialMediaDTO.builder()
                .id(entity.getId())
                .instagram(entity.getInstagram())
                .facebook(entity.getFacebook())
                .tiktok(entity.getTiktok())
                .build();
    }

    // CustomerSeo → CustomerSeoDTO
    public CustomerSeoDTO toSeoDTO(CustomerSeo entity) {
        return CustomerSeoDTO.builder()
                .id(entity.getId())
                .googleConsoleEmail(entity.getGoogleConsoleEmail())
                .titleSuggestions(entity.getSeoTitleSuggestions())
                .contentSuggestions(entity.getSeoContentSuggestions())
                .build();
    }

    // CustomerApiKey → CustomerApiKeyDTO
    public CustomerApiKeyDTO toApiKeyDTO(CustomerApiKey entity) {
        return CustomerApiKeyDTO.builder()
                .id(entity.getId())
                .instagramApiKey(entity.getInstagramApiKey())
                .facebookApiKey(entity.getFacebookApiKey())
                .tiktokApiKey(entity.getTiktokApiKey())
                .googleApiKey(entity.getGoogleApiKey())
                .build();
    }

    // ✅ CustomerMedia → CustomerMediaDTO
    public CustomerMediaDTO toMediaDTO(CustomerMedia entity) {
        // Config'den base URL'i al
        String baseUrl = fileUploadProperties.getBaseUrl();

        // Full URL oluştur: http://localhost:8080/uploads/premium-coffee-house/logos/logo.png
        String fullUrl = baseUrl + "/" + entity.getFilePath();

        return CustomerMediaDTO.builder()
                .id(entity.getId())
                .filePath(entity.getFilePath())
                .mediaType(entity.getMediaType())
                .originalFileName(entity.getOriginalFileName())
                .fileSize(entity.getFileSize())
                .fullUrl(fullUrl)
                .build();
    }

    // ========== HELPER METHODS ==========

    private boolean hasSocialMediaContent(CustomerSocialMediaDTO dto) {
        return isNotEmpty(dto.getInstagram()) ||
                isNotEmpty(dto.getFacebook()) ||
                isNotEmpty(dto.getTiktok());
    }

    private boolean hasSeoContent(CustomerSeoDTO dto) {
        return isNotEmpty(dto.getGoogleConsoleEmail()) ||
                isNotEmpty(dto.getTitleSuggestions()) ||
                isNotEmpty(dto.getContentSuggestions());
    }

    private boolean hasApiKeysContent(CustomerApiKeyDTO dto) {
        return isNotEmpty(dto.getInstagramApiKey()) ||
                isNotEmpty(dto.getFacebookApiKey()) ||
                isNotEmpty(dto.getTiktokApiKey()) ||
                isNotEmpty(dto.getGoogleApiKey());
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}