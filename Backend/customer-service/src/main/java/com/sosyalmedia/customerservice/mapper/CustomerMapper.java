package com.sosyalmedia.customerservice.mapper;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.entity.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    // CustomerRequest → Entity (CREATE için)
    public Customer toEntity(CustomerRequest request) {
        Customer customer = Customer.builder()
                .companyName(request.getCompanyName())
                .sector(request.getSector())
                .address(request.getAddress())
                .membershipPackage(request.getMembershipPackage())
                .status(request.getStatus())
                .build();

        // Target Audience
        CustomerTargetAudience targetAudience = CustomerTargetAudience.builder()
                .specialDates(request.getSpecialDates())
                .targetRegion(request.getTargetRegion())
                .customerHashtags(request.getCustomerHashtags())
                .postType(request.getPostType())
                .postFrequency(request.getPostFrequency())
                .postTone(request.getPostTone())
                .audienceAge(request.getAudienceAge())
                .audienceInterests(request.getAudienceInterests())
                .customer(customer)
                .build();
        customer.setTargetAudience(targetAudience);

        // Contacts - YENİ: Liste olarak
        if (request.getContacts() != null && !request.getContacts().isEmpty()) {
            for (int i = 0; i < request.getContacts().size(); i++) {
                ContactDTO contactDTO = request.getContacts().get(i);
                CustomerContact contact = CustomerContact.builder()
                        .name(contactDTO.getName())
                        .surname(contactDTO.getSurname())
                        .email(contactDTO.getEmail())
                        .phone(contactDTO.getPhone())
                        .priority(contactDTO.getPriority() != null ? contactDTO.getPriority() : i + 1)
                        .customer(customer)
                        .build();
                customer.getContacts().add(contact);
            }
        }

        // Social Media
        if (hasSocialMedia(request.getInstagram(), request.getFacebook(), request.getTiktok())) {
            CustomerSocialMedia socialMedia = CustomerSocialMedia.builder()
                    .instagram(request.getInstagram())
                    .facebook(request.getFacebook())
                    .tiktok(request.getTiktok())
                    .customer(customer)
                    .build();
            customer.setSocialMedia(socialMedia);
        }

        // SEO
        if (hasSeo(request.getGoogleConsoleEmail(), request.getSeoTitleSuggestions(), request.getSeoContentSuggestions())) {
            CustomerSeo seo = CustomerSeo.builder()
                    .googleConsoleEmail(request.getGoogleConsoleEmail())
                    .seoTitleSuggestions(request.getSeoTitleSuggestions())
                    .seoContentSuggestions(request.getSeoContentSuggestions())
                    .customer(customer)
                    .build();
            customer.setSeo(seo);
        }

        // API Keys
        if (hasApiKeys(request.getInstagramApiKey(), request.getFacebookApiKey(),
                request.getTiktokApiKey(), request.getGoogleApiKey())) {
            CustomerApiKey apiKey = CustomerApiKey.builder()
                    .instagramApiKey(request.getInstagramApiKey())
                    .facebookApiKey(request.getFacebookApiKey())
                    .tiktokApiKey(request.getTiktokApiKey())
                    .googleApiKey(request.getGoogleApiKey())
                    .customer(customer)
                    .build();
            customer.setApiKey(apiKey);
        }

        return customer;
    }

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
                .updatedAt(customer.getUpdatedAt());

        // Target Audience
        if (customer.getTargetAudience() != null) {
            CustomerTargetAudience ta = customer.getTargetAudience();
            builder.specialDates(ta.getSpecialDates())
                    .targetRegion(ta.getTargetRegion())
                    .customerHashtags(ta.getCustomerHashtags())
                    .postType(ta.getPostType())
                    .postFrequency(ta.getPostFrequency())
                    .postTone(ta.getPostTone())
                    .audienceAge(ta.getAudienceAge())
                    .audienceInterests(ta.getAudienceInterests());
        }

        // Contacts - YENİ: Liste olarak, priority'ye göre sıralı
        if (customer.getContacts() != null && !customer.getContacts().isEmpty()) {
            builder.contacts(customer.getContacts().stream()
                    .sorted(Comparator.comparing(CustomerContact::getPriority))
                    .map(this::toContactDTO)
                    .collect(Collectors.toList()));
        }

        // Social Media
        if (customer.getSocialMedia() != null) {
            builder.instagram(customer.getSocialMedia().getInstagram())
                    .facebook(customer.getSocialMedia().getFacebook())
                    .tiktok(customer.getSocialMedia().getTiktok());
        }

        // SEO
        if (customer.getSeo() != null) {
            builder.googleConsoleEmail(customer.getSeo().getGoogleConsoleEmail())
                    .seoTitleSuggestions(customer.getSeo().getSeoTitleSuggestions())
                    .seoContentSuggestions(customer.getSeo().getSeoContentSuggestions());
        }

        // API Keys
        if (customer.getApiKey() != null) {
            builder.instagramApiKey(customer.getApiKey().getInstagramApiKey())
                    .facebookApiKey(customer.getApiKey().getFacebookApiKey())
                    .tiktokApiKey(customer.getApiKey().getTiktokApiKey())
                    .googleApiKey(customer.getApiKey().getGoogleApiKey());
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

    // CustomerContact → ContactDTO
    public ContactDTO toContactDTO(CustomerContact contact) {
        return ContactDTO.builder()
                .name(contact.getName())
                .surname(contact.getSurname())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .priority(contact.getPriority())
                .build();
    }

    // Helper Methods
    private boolean hasSocialMedia(String instagram, String facebook, String tiktok) {
        return isNotEmpty(instagram) || isNotEmpty(facebook) || isNotEmpty(tiktok);
    }

    private boolean hasSeo(String email, String title, String content) {
        return isNotEmpty(email) || isNotEmpty(title) || isNotEmpty(content);
    }

    private boolean hasApiKeys(String instagram, String facebook, String tiktok, String google) {
        return isNotEmpty(instagram) || isNotEmpty(facebook) ||
                isNotEmpty(tiktok) || isNotEmpty(google);
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}