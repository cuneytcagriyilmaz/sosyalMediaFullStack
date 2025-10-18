// src/main/java/com/sosyalmedia/customerservice/service/impl/CustomerUpdateServiceImpl.java

package com.sosyalmedia.customerservice.service.impl;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.dto.request.BasicInfoUpdateRequest;
import com.sosyalmedia.customerservice.dto.response.CustomerResponse;
import com.sosyalmedia.customerservice.entity.*;
import com.sosyalmedia.customerservice.exception.CustomerNotFoundException;
import com.sosyalmedia.customerservice.mapper.CustomerMapper;
import com.sosyalmedia.customerservice.repository.ContactRepository;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import com.sosyalmedia.customerservice.service.CustomerUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUpdateServiceImpl implements CustomerUpdateService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public CustomerResponse updateBasicInfo(Long customerId, BasicInfoUpdateRequest request) {
        log.info("Updating basic info for customer ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Temel bilgileri güncelle
        customer.setCompanyName(request.getCompanyName());
        customer.setSector(request.getSector());
        customer.setAddress(request.getAddress());
        customer.setMembershipPackage(request.getMembershipPackage());
        customer.setStatus(request.getStatus());

        Customer saved = customerRepository.save(customer);
        log.info("✅ Basic info updated for customer ID: {}", customerId);

        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponse updateContacts(Long customerId, List<CustomerContactDTO> contactDTOs) {
        log.info("🔄 Updating contacts for customer ID: {}", customerId);
        log.info("📊 New contacts count: {}", contactDTOs.size());

        Customer customer = customerRepository.findByIdWithAllRelations(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        log.info("📊 Old contacts count: {}", customer.getContacts().size());

        //  DUPLICATE KONTROLÜ
        validateNoDuplicateContacts(contactDTOs);

        //  ESKİ CONTACT'LARI TAMAMEN SİL (Repository ile)
        if (!customer.getContacts().isEmpty()) {
            log.info("🗑️ Deleting {} old contacts...", customer.getContacts().size());
            contactRepository.deleteAll(customer.getContacts());
            customer.getContacts().clear();
            log.info("✅ Old contacts deleted");
        }

        //  YENİ CONTACT'LARI EKLE
        log.info("➕ Adding {} new contacts...", contactDTOs.size());
        for (int i = 0; i < contactDTOs.size(); i++) {
            CustomerContactDTO dto = contactDTOs.get(i);
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

        Customer saved = customerRepository.save(customer);
        log.info("✅ Contacts updated - Final count: {}", saved.getContacts().size());

        return customerMapper.toResponse(saved);
    }


    @Override
    @Transactional
    public CustomerResponse updateSocialMedia(Long customerId, CustomerSocialMediaDTO dto) {
        log.info("Updating social media for customer ID: {}", customerId);

        Customer customer = customerRepository.findByIdWithAllRelations(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerSocialMedia sm = customer.getSocialMedia();
        if (sm == null) {
            sm = CustomerSocialMedia.builder().customer(customer).build();
            customer.setSocialMedia(sm);
        }

        // Sosyal medya hesaplarını güncelle
        sm.setInstagram(dto.getInstagram());
        sm.setFacebook(dto.getFacebook());
        sm.setTiktok(dto.getTiktok());

        Customer saved = customerRepository.save(customer);
        log.info("✅ Social media updated for customer ID: {}", customerId);

        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponse updateTargetAudience(Long customerId, CustomerTargetAudienceDTO dto) {
        log.info("Updating target audience for customer ID: {}", customerId);

        Customer customer = customerRepository.findByIdWithAllRelations(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerTargetAudience ta = customer.getTargetAudience();
        if (ta == null) {
            ta = CustomerTargetAudience.builder().customer(customer).build();
            customer.setTargetAudience(ta);
        }

        // Hedef kitle bilgilerini güncelle
        ta.setSpecialDates(dto.getSpecialDates());
        ta.setTargetRegion(dto.getTargetRegion());
        ta.setCustomerHashtags(dto.getCustomerHashtags());
        ta.setPostType(dto.getPostType());
        ta.setPostFrequency(dto.getPostFrequency());
        ta.setPostTone(dto.getPostTone());
        ta.setAudienceAge(dto.getAudienceAge());
        ta.setAudienceInterests(dto.getAudienceInterests());

        Customer saved = customerRepository.save(customer);
        log.info("✅ Target audience updated for customer ID: {}", customerId);

        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponse updateSeo(Long customerId, CustomerSeoDTO dto) {
        log.info("Updating SEO for customer ID: {}", customerId);

        Customer customer = customerRepository.findByIdWithAllRelations(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerSeo seo = customer.getSeo();
        if (seo == null) {
            seo = CustomerSeo.builder().customer(customer).build();
            customer.setSeo(seo);
        }

        // SEO bilgilerini güncelle
        seo.setGoogleConsoleEmail(dto.getGoogleConsoleEmail());
        seo.setSeoTitleSuggestions(dto.getTitleSuggestions());
        seo.setSeoContentSuggestions(dto.getContentSuggestions());

        Customer saved = customerRepository.save(customer);
        log.info("✅ SEO updated for customer ID: {}", customerId);

        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponse updateApiKeys(Long customerId, CustomerApiKeyDTO dto) {
        log.info("Updating API keys for customer ID: {}", customerId);

        Customer customer = customerRepository.findByIdWithAllRelations(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerApiKey ak = customer.getApiKey();
        if (ak == null) {
            ak = CustomerApiKey.builder().customer(customer).build();
            customer.setApiKey(ak);
        }

        // API anahtarlarını güncelle
        ak.setInstagramApiKey(dto.getInstagramApiKey());
        ak.setFacebookApiKey(dto.getFacebookApiKey());
        ak.setTiktokApiKey(dto.getTiktokApiKey());
        ak.setGoogleApiKey(dto.getGoogleApiKey());

        Customer saved = customerRepository.save(customer);
        log.info("✅ API keys updated for customer ID: {}", customerId);

        return customerMapper.toResponse(saved);
    }

    // ========== HELPER METHODS ==========

    /**
     * Contact listesinde duplicate email veya telefon kontrolü yapar
     */
    private void validateNoDuplicateContacts(List<CustomerContactDTO> contacts) {
        Set<String> emails = new HashSet<>();
        Set<String> phones = new HashSet<>();

        for (CustomerContactDTO contact : contacts) {
            // Email duplicate kontrolü
            if (contact.getEmail() != null && !contact.getEmail().isBlank()) {
                String email = contact.getEmail().toLowerCase().trim();
                if (emails.contains(email)) {
                    log.error("❌ Duplicate email found: {}", email);
                    throw new IllegalArgumentException("Duplicate email found: " + email);
                }
                emails.add(email);
            }

            // Telefon duplicate kontrolü
            if (contact.getPhone() != null && !contact.getPhone().isBlank()) {
                String phone = contact.getPhone().replaceAll("[^0-9]", ""); // Sadece rakamlar
                if (phones.contains(phone)) {
                    log.error("❌ Duplicate phone found: {}", contact.getPhone());
                    throw new IllegalArgumentException("Duplicate phone found: " + contact.getPhone());
                }
                phones.add(phone);
            }
        }

        log.info("✅ No duplicate contacts found");
    }
}