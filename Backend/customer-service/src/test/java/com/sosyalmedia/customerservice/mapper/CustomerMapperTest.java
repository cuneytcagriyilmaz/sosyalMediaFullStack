package com.sosyalmedia.customerservice.mapper;

import com.sosyalmedia.customerservice.dto.CustomerContactDTO;
import com.sosyalmedia.customerservice.dto.CustomerRequest;
import com.sosyalmedia.customerservice.dto.CustomerResponse;
import com.sosyalmedia.customerservice.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        List<CustomerContactDTO> contacts = new ArrayList<>();
        contacts.add(CustomerContactDTO.builder()
                .name("Ahmet")
                .surname("Yilmaz")
                .email("test@test.com")
                .phone("5551234567")
                .priority(1)
                .build());

        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .specialDates(false)
                .contacts(contacts)
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
    void toEntity_shouldMapContactsList() {
        // Given
        List<CustomerContactDTO> contacts = new ArrayList<>();
        contacts.add(CustomerContactDTO.builder()
                .name("Ahmet")
                .surname("Yilmaz")
                .email("ahmet@test.com")
                .phone("5551234567")
                .priority(1)
                .build());
        contacts.add(CustomerContactDTO.builder()
                .name("Ayse")
                .surname("Kaya")
                .email("ayse@test.com")
                .phone("5557654321")
                .priority(2)
                .build());

        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Company")
                .sector("IT")
                .address("Test Address")
                .membershipPackage("Gold")
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .specialDates(false)
                .contacts(contacts)
                .build();

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertNotNull(customer.getContacts());
        assertEquals(2, customer.getContacts().size());
    }

    @Test
    void toResponse_shouldMapContactsList() {
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
        contacts.add(CustomerContact.builder()
                .name("Ayse")
                .surname("Kaya")
                .email("ayse@test.com")
                .phone("5559876543")
                .priority(2)
                .customer(customer)
                .build());
        customer.setContacts(contacts);

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
        assertNotNull(response.getContacts());
        assertEquals(2, response.getContacts().size());
        assertEquals("Ahmet", response.getContacts().get(0).getName());
        assertEquals("Ayse", response.getContacts().get(1).getName());
    }

    @Test
    void toContactDTO_shouldMapFields() {
        // Given
        CustomerContact contact = CustomerContact.builder()
                .name("Ahmet")
                .surname("Yilmaz")
                .email("ahmet@test.com")
                .phone("5551234567")
                .priority(1)
                .build();

        // When
        CustomerContactDTO dto = customerMapper.toContactDTO(contact);

        // Then
        assertNotNull(dto);
        assertEquals("Ahmet", dto.getName());
        assertEquals("Yilmaz", dto.getSurname());
        assertEquals("ahmet@test.com", dto.getEmail());
        assertEquals("5551234567", dto.getPhone());
        assertEquals(1, dto.getPriority());
    }
}