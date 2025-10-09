package com.sosyalmedia.customerservice.mapper;

import com.sosyalmedia.customerservice.dto.CustomerRequest;
import com.sosyalmedia.customerservice.dto.CustomerResponse;
import com.sosyalmedia.customerservice.dto.CustomerListResponse;
import com.sosyalmedia.customerservice.entity.*;
import org.springframework.stereotype.Component;

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

        // Contacts
        addContactIfValid(customer,
                request.getCustomerContact1Name(),
                request.getCustomerContact1Surname(),
                request.getCustomerContact1Email(),
                request.getCustomerContact1Phone(), 1);

        addContactIfValid(customer,
                request.getCustomerContact2Name(),
                request.getCustomerContact2Surname(),
                request.getCustomerContact2Email(),
                request.getCustomerContact2Phone(), 2);

        addContactIfValid(customer,
                request.getCustomerContact3Name(),
                request.getCustomerContact3Surname(),
                request.getCustomerContact3Email(),
                request.getCustomerContact3Phone(), 3);

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

        // Contacts
        for (CustomerContact contact : customer.getContacts()) {
            switch (contact.getPriority()) {
                case 1:
                    builder.customerContact1Name(contact.getName())
                            .customerContact1Surname(contact.getSurname())
                            .customerContact1Email(contact.getEmail())
                            .customerContact1Phone(contact.getPhone());
                    break;
                case 2:
                    builder.customerContact2Name(contact.getName())
                            .customerContact2Surname(contact.getSurname())
                            .customerContact2Email(contact.getEmail())
                            .customerContact2Phone(contact.getPhone());
                    break;
                case 3:
                    builder.customerContact3Name(contact.getName())
                            .customerContact3Surname(contact.getSurname())
                            .customerContact3Email(contact.getEmail())
                            .customerContact3Phone(contact.getPhone());
                    break;
            }
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

    // Helper Methods
    private void addContactIfValid(Customer customer, String name, String surname,
                                   String email, String phone, int priority) {
        if (isNotEmpty(name) && isNotEmpty(surname) && isNotEmpty(email) && isNotEmpty(phone)) {
            CustomerContact contact = CustomerContact.builder()
                    .name(name)
                    .surname(surname)
                    .email(email)
                    .phone(phone)
                    .priority(priority)
                    .customer(customer)
                    .build();
            customer.getContacts().add(contact);
        }
    }

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