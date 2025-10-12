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

        // Target Audience
        updateTargetAudience(customer, request);

        // Contacts - YENİ: Liste olarak güncelleme
        if (request.getContacts() != null) {
            updateContacts(customer, request.getContacts());
        }

        // Social Media
        updateSocialMedia(customer, request);

        // SEO
        updateSeo(customer, request);

        // API Keys
        updateApiKeys(customer, request);
    }

    private void updateTargetAudience(Customer customer, CustomerUpdateRequest request) {
        CustomerTargetAudience ta = customer.getTargetAudience();
        if (ta == null) {
            ta = CustomerTargetAudience.builder().customer(customer).build();
            customer.setTargetAudience(ta);
        }

        if (request.getSpecialDates() != null) ta.setSpecialDates(request.getSpecialDates());
        if (request.getTargetRegion() != null) ta.setTargetRegion(request.getTargetRegion());
        if (request.getCustomerHashtags() != null) ta.setCustomerHashtags(request.getCustomerHashtags());
        if (request.getPostType() != null) ta.setPostType(request.getPostType());
        if (request.getPostFrequency() != null) ta.setPostFrequency(request.getPostFrequency());
        if (request.getPostTone() != null) ta.setPostTone(request.getPostTone());
        if (request.getAudienceAge() != null) ta.setAudienceAge(request.getAudienceAge());
        if (request.getAudienceInterests() != null) ta.setAudienceInterests(request.getAudienceInterests());
    }

    private void updateContacts(Customer customer, List<ContactDTO> contactDTOs) {
        // Mevcut contact'ları temizle
        customer.getContacts().clear();

        // Yeni contact'ları ekle
        for (int i = 0; i < contactDTOs.size(); i++) {
            ContactDTO contactDTO = contactDTOs.get(i);
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

    private void updateSocialMedia(Customer customer, CustomerUpdateRequest request) {
        CustomerSocialMedia sm = customer.getSocialMedia();
        if (sm == null && hasAnySocialMedia(request)) {
            sm = CustomerSocialMedia.builder().customer(customer).build();
            customer.setSocialMedia(sm);
        }

        if (sm != null) {
            if (request.getInstagram() != null) sm.setInstagram(request.getInstagram());
            if (request.getFacebook() != null) sm.setFacebook(request.getFacebook());
            if (request.getTiktok() != null) sm.setTiktok(request.getTiktok());
        }
    }

    private void updateSeo(Customer customer, CustomerUpdateRequest request) {
        CustomerSeo seo = customer.getSeo();
        if (seo == null && hasAnySeo(request)) {
            seo = CustomerSeo.builder().customer(customer).build();
            customer.setSeo(seo);
        }

        if (seo != null) {
            if (request.getGoogleConsoleEmail() != null) {
                seo.setGoogleConsoleEmail(request.getGoogleConsoleEmail());
            }
            if (request.getSeoTitleSuggestions() != null) {
                seo.setSeoTitleSuggestions(request.getSeoTitleSuggestions());
            }
            if (request.getSeoContentSuggestions() != null) {
                seo.setSeoContentSuggestions(request.getSeoContentSuggestions());
            }
        }
    }

    private void updateApiKeys(Customer customer, CustomerUpdateRequest request) {
        CustomerApiKey ak = customer.getApiKey();
        if (ak == null && hasAnyApiKey(request)) {
            ak = CustomerApiKey.builder().customer(customer).build();
            customer.setApiKey(ak);
        }

        if (ak != null) {
            if (request.getInstagramApiKey() != null) ak.setInstagramApiKey(request.getInstagramApiKey());
            if (request.getFacebookApiKey() != null) ak.setFacebookApiKey(request.getFacebookApiKey());
            if (request.getTiktokApiKey() != null) ak.setTiktokApiKey(request.getTiktokApiKey());
            if (request.getGoogleApiKey() != null) ak.setGoogleApiKey(request.getGoogleApiKey());
        }
    }

    private boolean hasAnySocialMedia(CustomerUpdateRequest request) {
        return request.getInstagram() != null ||
                request.getFacebook() != null ||
                request.getTiktok() != null;
    }

    private boolean hasAnySeo(CustomerUpdateRequest request) {
        return request.getGoogleConsoleEmail() != null ||
                request.getSeoTitleSuggestions() != null ||
                request.getSeoContentSuggestions() != null;
    }

    private boolean hasAnyApiKey(CustomerUpdateRequest request) {
        return request.getInstagramApiKey() != null ||
                request.getFacebookApiKey() != null ||
                request.getTiktokApiKey() != null ||
                request.getGoogleApiKey() != null;
    }

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