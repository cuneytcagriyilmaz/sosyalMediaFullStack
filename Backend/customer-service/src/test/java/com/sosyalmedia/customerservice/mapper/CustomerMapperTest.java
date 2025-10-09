package com.sosyalmedia.customerservice.mapper;


import com.sosyalmedia.customerservice.dto.CustomerRequest;
import com.sosyalmedia.customerservice.dto.CustomerResponse;
import com.sosyalmedia.customerservice.dto.CustomerListResponse;
import com.sosyalmedia.customerservice.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void toEntity_shouldMapBasicFields() {
        // Given
        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .customerContact1Name("Ahmet")
                .customerContact1Surname("Yilmaz")
                .customerContact1Email("test@test.com")
                .customerContact1Phone("5551234567")
                .build();

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertNotNull(customer);
        assertEquals("Test Company", customer.getCompanyName());
        assertEquals("IT", customer.getSector());
        assertEquals("Test Address", customer.getAddress());
        assertEquals("Gold", customer.getMembershipPackage());
        assertEquals(Customer.CustomerStatus.ACTIVE, customer.getStatus());
    }

    @Test
    void toEntity_shouldMapTargetAudience() {
        // Given
        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .specialDates(true)
                .targetRegion("Istanbul")
                .customerHashtags("#test #company")
                .audienceAge("25-45")
                .audienceInterests("Technology, Software")
                .customerContact1Name("Ahmet")
                .customerContact1Surname("Yilmaz")
                .customerContact1Email("test@test.com")
                .customerContact1Phone("5551234567")
                .build();

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertNotNull(customer.getTargetAudience());
        assertEquals(true, customer.getTargetAudience().getSpecialDates());
        assertEquals("Istanbul", customer.getTargetAudience().getTargetRegion());
        assertEquals("#test #company", customer.getTargetAudience().getCustomerHashtags());
        assertEquals("gorsel", customer.getTargetAudience().getPostType());
        assertEquals("3", customer.getTargetAudience().getPostFrequency());
        assertEquals("samimi", customer.getTargetAudience().getPostTone());
        assertEquals("25-45", customer.getTargetAudience().getAudienceAge());
    }

    @Test
    void toEntity_shouldMapContacts() {
        // Given
        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .customerContact1Name("Ahmet")
                .customerContact1Surname("Yilmaz")
                .customerContact1Email("ahmet@test.com")
                .customerContact1Phone("5551234567")
                .customerContact2Name("Ayse")
                .customerContact2Surname("Kaya")
                .customerContact2Email("ayse@test.com")
                .customerContact2Phone("5557654321")
                .build();

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertNotNull(customer.getContacts());
        assertEquals(2, customer.getContacts().size());
    }

    @Test
    void toEntity_shouldMapSocialMedia() {
        // Given
        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .instagram("@testcompany")
                .facebook("TestCompanyPage")
                .tiktok("@testcompanytiktok")
                .customerContact1Name("Ahmet")
                .customerContact1Surname("Yilmaz")
                .customerContact1Email("test@test.com")
                .customerContact1Phone("5551234567")
                .build();

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertNotNull(customer.getSocialMedia());
        assertEquals("@testcompany", customer.getSocialMedia().getInstagram());
        assertEquals("TestCompanyPage", customer.getSocialMedia().getFacebook());
        assertEquals("@testcompanytiktok", customer.getSocialMedia().getTiktok());
    }

    @Test
    void toResponse_shouldMapBasicFields() {
        // Given
        Customer customer = Customer.builder()
                .id(1L)
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .contacts(new HashSet<>())
                .media(new HashSet<>())
                .build();

        CustomerTargetAudience targetAudience = CustomerTargetAudience.builder()
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .specialDates(false)
                .customer(customer)
                .build();
        customer.setTargetAudience(targetAudience);

        // When
        CustomerResponse response = customerMapper.toResponse(customer);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Company", response.getCompanyName());
        assertEquals("IT", response.getSector());
        assertEquals("Test Address", response.getAddress());
        assertEquals("Gold", response.getMembershipPackage());
        assertEquals(Customer.CustomerStatus.ACTIVE, response.getStatus());
    }

    @Test
    void toResponse_shouldMapContacts() {
        // Given
        Customer customer = Customer.builder()
                .id(1L)
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .media(new HashSet<>())
                .build();

        Set<CustomerContact> contacts = new HashSet<>();
        contacts.add(CustomerContact.builder()
                .name("Ahmet")
                .surname("Yilmaz")
                .email("ahmet@test.com")
                .phone("5551234567")
                .priority(1)
                .customer(customer)
                .build());
        customer.setContacts(contacts);

        CustomerTargetAudience targetAudience = CustomerTargetAudience.builder()
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .customer(customer)
                .build();
        customer.setTargetAudience(targetAudience);

        // When
        CustomerResponse response = customerMapper.toResponse(customer);

        // Then
        assertEquals("Ahmet", response.getCustomerContact1Name());
        assertEquals("Yilmaz", response.getCustomerContact1Surname());
        assertEquals("ahmet@test.com", response.getCustomerContact1Email());
        assertEquals("5551234567", response.getCustomerContact1Phone());
    }

    @Test
    void toListResponse_shouldMapFields() {
        // Given
        Customer customer = Customer.builder()
                .id(1L)
                .companyName("Test Company")
                .sector("IT")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        // When
        CustomerListResponse response = customerMapper.toListResponse(customer);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Company", response.getCompanyName());
        assertEquals("IT", response.getSector());
        assertEquals("Gold", response.getMembershipPackage());
        assertEquals(Customer.CustomerStatus.ACTIVE, response.getStatus());
    }
}