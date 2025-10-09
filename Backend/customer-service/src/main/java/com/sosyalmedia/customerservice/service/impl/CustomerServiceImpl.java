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
        Customer customer = customerRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        applyPatch(customer, request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse patchCustomerByCompanyName(String companyName, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findByCompanyNameWithAllRelations(companyName)
                .orElseThrow(() -> new CustomerNotFoundException("companyName", companyName));

        applyPatch(customer, request);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    private void applyPatch(Customer customer, CustomerUpdateRequest request) {
        if (request.getCompanyName() != null) customer.setCompanyName(request.getCompanyName());
        if (request.getSector() != null) customer.setSector(request.getSector());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getMembershipPackage() != null) customer.setMembershipPackage(request.getMembershipPackage());
        if (request.getStatus() != null) customer.setStatus(request.getStatus());

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

        updateOrAddContact(customer, request.getCustomerContact1Name(), request.getCustomerContact1Surname(),
                request.getCustomerContact1Email(), request.getCustomerContact1Phone(), 1);
        updateOrAddContact(customer, request.getCustomerContact2Name(), request.getCustomerContact2Surname(),
                request.getCustomerContact2Email(), request.getCustomerContact2Phone(), 2);
        updateOrAddContact(customer, request.getCustomerContact3Name(), request.getCustomerContact3Surname(),
                request.getCustomerContact3Email(), request.getCustomerContact3Phone(), 3);

        CustomerSocialMedia sm = customer.getSocialMedia();
        if (sm == null) {
            sm = CustomerSocialMedia.builder().customer(customer).build();
            customer.setSocialMedia(sm);
        }
        if (request.getInstagram() != null) sm.setInstagram(request.getInstagram());
        if (request.getFacebook() != null) sm.setFacebook(request.getFacebook());
        if (request.getTiktok() != null) sm.setTiktok(request.getTiktok());

        CustomerSeo seo = customer.getSeo();
        if (seo == null) {
            seo = CustomerSeo.builder().customer(customer).build();
            customer.setSeo(seo);
        }
        if (request.getGoogleConsoleEmail() != null) seo.setGoogleConsoleEmail(request.getGoogleConsoleEmail());
        if (request.getSeoTitleSuggestions() != null) seo.setSeoTitleSuggestions(request.getSeoTitleSuggestions());
        if (request.getSeoContentSuggestions() != null) seo.setSeoContentSuggestions(request.getSeoContentSuggestions());

        CustomerApiKey ak = customer.getApiKey();
        if (ak == null) {
            ak = CustomerApiKey.builder().customer(customer).build();
            customer.setApiKey(ak);
        }
        if (request.getInstagramApiKey() != null) ak.setInstagramApiKey(request.getInstagramApiKey());
        if (request.getFacebookApiKey() != null) ak.setFacebookApiKey(request.getFacebookApiKey());
        if (request.getTiktokApiKey() != null) ak.setTiktokApiKey(request.getTiktokApiKey());
        if (request.getGoogleApiKey() != null) ak.setGoogleApiKey(request.getGoogleApiKey());
    }

    private void updateOrAddContact(Customer customer, String name, String surname, String email, String phone, int priority) {
        if (name == null && surname == null && email == null && phone == null) return;

        CustomerContact existing = customer.getContacts().stream()
                .filter(c -> c.getPriority() == priority)
                .findFirst()
                .orElse(null);

        if (existing != null) {
            if (name != null) existing.setName(name);
            if (surname != null) existing.setSurname(surname);
            if (email != null) existing.setEmail(email);
            if (phone != null) existing.setPhone(phone);
        } else if (name != null && surname != null && email != null && phone != null) {
            customer.getContacts().add(CustomerContact.builder()
                    .name(name).surname(surname).email(email).phone(phone)
                    .priority(priority).customer(customer).build());
        }
    }

    @Override
    @Transactional
    public void softDeleteCustomer(Long id) {
        log.info("Soft deleting customer ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // 1. Müşteri klasörünü taşı
        fileStorageService.moveCustomerFolderToDeleted(customer.getCompanyName());

        // Manuel soft delete - @SQLDelete yerine
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

        // Manuel soft delete
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

        // Child kayıtları manuel sil
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

        // Son olarak customer'ı sil
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