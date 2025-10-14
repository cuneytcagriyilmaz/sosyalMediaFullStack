package com.sosyalmedia.customerservice.service.impl;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.entity.*;
import com.sosyalmedia.customerservice.exception.CustomerAlreadyExistsException;
import com.sosyalmedia.customerservice.exception.CustomerNotFoundException;
import com.sosyalmedia.customerservice.mapper.CustomerMapper;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import com.sosyalmedia.customerservice.service.CustomerService;
import com.sosyalmedia.customerservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final FileStorageService fileStorageService;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating customer: {}", request.getCompanyName());

        if (customerRepository.findByCompanyName(request.getCompanyName()).isPresent()) {
            throw new CustomerAlreadyExistsException(request.getCompanyName());
        }

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created with ID: {}", savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCompanyName(String companyName) {
        Customer customer = customerRepository.findByCompanyNameWithAllRelations(companyName)
                .orElseThrow(() -> new CustomerNotFoundException("companyName", companyName));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerListResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerListResponse> getCustomersBySector(String sector) {
        return customerRepository.findBySector(sector).stream()
                .map(customerMapper::toListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerListResponse> getCustomersByStatus(Customer.CustomerStatus status) {
        return customerRepository.findByStatus(status).stream()
                .map(customerMapper::toListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse patchCustomer(Long id, CustomerUpdateRequest request) {
        log.info("Patching customer ID: {}", id);

        Customer customer = customerRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        applyPatch(customer, request);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer patched: {}", id);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse patchCustomerByCompanyName(String companyName, CustomerUpdateRequest request) {
        log.info("Patching customer by company name: {}", companyName);

        Customer customer = customerRepository.findByCompanyNameWithAllRelations(companyName)
                .orElseThrow(() -> new CustomerNotFoundException("companyName", companyName));

        applyPatch(customer, request);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer patched: {}", companyName);
        return customerMapper.toResponse(savedCustomer);
    }

    private void applyPatch(Customer customer, CustomerUpdateRequest request) {
        // Basic fields
        if (request.getCompanyName() != null) {
            customer.setCompanyName(request.getCompanyName());
        }
        if (request.getSector() != null) {
            customer.setSector(request.getSector());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        if (request.getMembershipPackage() != null) {
            customer.setMembershipPackage(request.getMembershipPackage());
        }
        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }

        // ✅ NESTED DTO'LARDAN GÜNCELLEME

        // Target Audience - YENİ: Nested DTO'dan güncelle
        if (request.getTargetAudience() != null) {
            updateTargetAudience(customer, request.getTargetAudience());
        }

        // Contacts - Liste olarak güncelleme
        if (request.getContacts() != null) {
            updateContacts(customer, request.getContacts());
        }

        // Social Media - YENİ: Nested DTO'dan güncelle
        if (request.getSocialMedia() != null) {
            updateSocialMedia(customer, request.getSocialMedia());
        }

        // SEO - YENİ: Nested DTO'dan güncelle
        if (request.getSeo() != null) {
            updateSeo(customer, request.getSeo());
        }

        // API Keys - YENİ: Nested DTO'dan güncelle
        if (request.getApiKeys() != null) {
            updateApiKeys(customer, request.getApiKeys());
        }
    }

    // ========== NESTED DTO UPDATE METHODS ==========

    private void updateTargetAudience(Customer customer, CustomerTargetAudienceDTO dto) {
        CustomerTargetAudience ta = customer.getTargetAudience();
        if (ta == null) {
            ta = CustomerTargetAudience.builder().customer(customer).build();
            customer.setTargetAudience(ta);
        }

        // DTO'dan gelen tüm alanları güncelle (null kontrolü yaparak)
        if (dto.getSpecialDates() != null) ta.setSpecialDates(dto.getSpecialDates());
        if (dto.getTargetRegion() != null) ta.setTargetRegion(dto.getTargetRegion());
        if (dto.getCustomerHashtags() != null) ta.setCustomerHashtags(dto.getCustomerHashtags());
        if (dto.getPostType() != null) ta.setPostType(dto.getPostType());
        if (dto.getPostFrequency() != null) ta.setPostFrequency(dto.getPostFrequency());
        if (dto.getPostTone() != null) ta.setPostTone(dto.getPostTone());
        if (dto.getAudienceAge() != null) ta.setAudienceAge(dto.getAudienceAge());
        if (dto.getAudienceInterests() != null) ta.setAudienceInterests(dto.getAudienceInterests());
    }

    private void updateContacts(Customer customer, List<CustomerContactDTO> contactDTOs) {
        // Mevcut contact'ları temizle
        customer.getContacts().clear();

        // Yeni contact'ları ekle
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
    }

    private void updateSocialMedia(Customer customer, CustomerSocialMediaDTO dto) {
        CustomerSocialMedia sm = customer.getSocialMedia();
        if (sm == null) {
            sm = CustomerSocialMedia.builder().customer(customer).build();
            customer.setSocialMedia(sm);
        }

        // DTO'dan gelen tüm alanları güncelle (null kontrolü yaparak)
        if (dto.getInstagram() != null) sm.setInstagram(dto.getInstagram());
        if (dto.getFacebook() != null) sm.setFacebook(dto.getFacebook());
        if (dto.getTiktok() != null) sm.setTiktok(dto.getTiktok());
    }

    private void updateSeo(Customer customer, CustomerSeoDTO dto) {
        CustomerSeo seo = customer.getSeo();
        if (seo == null) {
            seo = CustomerSeo.builder().customer(customer).build();
            customer.setSeo(seo);
        }

        // DTO'dan gelen tüm alanları güncelle (null kontrolü yaparak)
        if (dto.getGoogleConsoleEmail() != null) {
            seo.setGoogleConsoleEmail(dto.getGoogleConsoleEmail());
        }
        if (dto.getTitleSuggestions() != null) {
            seo.setSeoTitleSuggestions(dto.getTitleSuggestions());
        }
        if (dto.getContentSuggestions() != null) {
            seo.setSeoContentSuggestions(dto.getContentSuggestions());
        }
    }

    private void updateApiKeys(Customer customer, CustomerApiKeyDTO dto) {
        CustomerApiKey ak = customer.getApiKey();
        if (ak == null) {
            ak = CustomerApiKey.builder().customer(customer).build();
            customer.setApiKey(ak);
        }

        // DTO'dan gelen tüm alanları güncelle (null kontrolü yaparak)
        if (dto.getInstagramApiKey() != null) ak.setInstagramApiKey(dto.getInstagramApiKey());
        if (dto.getFacebookApiKey() != null) ak.setFacebookApiKey(dto.getFacebookApiKey());
        if (dto.getTiktokApiKey() != null) ak.setTiktokApiKey(dto.getTiktokApiKey());
        if (dto.getGoogleApiKey() != null) ak.setGoogleApiKey(dto.getGoogleApiKey());
    }

    // ========== SOFT/HARD DELETE METHODS (Değişiklik yok) ==========

    @Override
    @Transactional
    public void softDeleteCustomer(Long id) {
        log.info("Soft deleting customer ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // 1. Müşteri klasörünü taşı
        fileStorageService.moveCustomerFolderToDeleted(customer.getCompanyName());

        // 2. Manuel soft delete
        customer.setDeleted(true);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);

        log.info("Customer soft deleted: {}", id);
    }

    @Override
    @Transactional
    public void softDeleteCustomerByCompanyName(String companyName) {
        log.info("Soft deleting customer by company name: {}", companyName);

        Customer customer = customerRepository.findByCompanyName(companyName)
                .orElseThrow(() -> new CustomerNotFoundException("companyName", companyName));

        // 1. Müşteri klasörünü taşı
        fileStorageService.moveCustomerFolderToDeleted(customer.getCompanyName());

        // 2. Manuel soft delete
        customer.setDeleted(true);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);

        log.info("Customer soft deleted with name: {}", companyName);
    }

    @Override
    @Transactional
    public void hardDeleteCustomer(Long id) {
        log.info("Hard deleting customer ID: {}", id);

        // Önce müşteriyi bul (deleted=true olanları da dahil et)
        Customer customer = entityManager.createQuery(
                        "SELECT c FROM Customer c WHERE c.id = :id", Customer.class)
                .setParameter("id", id)
                .getSingleResult();

        // 1. Müşteri klasörünü tamamen sil
        fileStorageService.deleteCustomerFolder(customer.getCompanyName());

        // 2. Child kayıtları manuel sil
        if (customer.getApiKey() != null) {
            entityManager.remove(customer.getApiKey());
        }
        if (customer.getSeo() != null) {
            entityManager.remove(customer.getSeo());
        }
        if (customer.getSocialMedia() != null) {
            entityManager.remove(customer.getSocialMedia());
        }
        if (customer.getTargetAudience() != null) {
            entityManager.remove(customer.getTargetAudience());
        }

        // Contacts ve Media
        customer.getContacts().forEach(entityManager::remove);
        customer.getMedia().forEach(entityManager::remove);

        // 3. Customer'ı sil
        entityManager.remove(customer);
        entityManager.flush();

        log.info("Customer hard deleted: {}", id);
    }

    @Override
    @Transactional
    public void restoreCustomer(Long id) {
        log.info("Restoring customer ID: {}", id);

        // @SQLRestriction bypass etmek için native query
        List<?> resultList = entityManager
                .createNativeQuery("SELECT * FROM customers WHERE id = :id", Customer.class)
                .setParameter("id", id)
                .getResultList();

        if (resultList.isEmpty()) {
            throw new CustomerNotFoundException("Müşteri bulunamadı: " + id);
        }

        Customer customer = (Customer) resultList.get(0);

        if (!customer.getDeleted()) {
            throw new IllegalStateException("Müşteri zaten aktif durumda: " + id);
        }

        // 1. Müşteri klasörünü geri getir
        fileStorageService.restoreCustomerFolder(customer.getCompanyName());

        // 2. DB'de restore
        customer.setDeleted(false);
        customer.setDeletedAt(null);
        customerRepository.save(customer);

        log.info("Customer restored: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllDeletedCustomers() {
        return customerRepository.findAllDeleted().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }
}