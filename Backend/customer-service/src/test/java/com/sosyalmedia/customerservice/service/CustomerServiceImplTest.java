package com.sosyalmedia.customerservice.service;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.dto.request.CustomerRequest;
import com.sosyalmedia.customerservice.dto.request.CustomerUpdateRequest;
import com.sosyalmedia.customerservice.dto.response.CustomerResponse;
import com.sosyalmedia.customerservice.entity.*;
import com.sosyalmedia.customerservice.exception.*;
import com.sosyalmedia.customerservice.mapper.CustomerMapper;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import com.sosyalmedia.customerservice.service.impl.CustomerServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest request;
    private Customer customer;
    private CustomerResponse response;

    @BeforeEach
    void setUp() {
        // ContactDTO listesi oluştur
        List<CustomerContactDTO> contacts = new ArrayList<>();
        contacts.add(CustomerContactDTO.builder()
                .name("Ahmet")
                .surname("Yılmaz")
                .email("ahmet@test.com")
                .phone("5551234567")
                .priority(1)
                .build());

        request = CustomerRequest.builder()
                .companyName("Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .specialDates(false)
                .postFrequency("2")
                .postType("gorsel")
                .postTone("samimi")
                .contacts(contacts)  // YENİ: Liste olarak
                .build();

        customer = Customer.builder()
                .id(1L)
                .companyName("Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .deleted(false)
                .build();

        response = CustomerResponse.builder()
                .id(1L)
                .companyName("Test Cafe")
                .sector("cafe")
                .contacts(contacts)  // YENİ: Liste olarak
                .build();
    }

    @Test
    void createCustomer_Success() {
        when(customerRepository.findByCompanyName(anyString())).thenReturn(Optional.empty());
        when(customerMapper.toEntity(any(CustomerRequest.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(response);

        CustomerResponse result = customerService.createCustomer(request);

        assertThat(result).isNotNull();
        assertThat(result.getCompanyName()).isEqualTo("Test Cafe");
        assertThat(result.getContacts()).hasSize(1);
        assertThat(result.getContacts().get(0).getName()).isEqualTo("Ahmet");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_AlreadyExists_ThrowsException() {
        when(customerRepository.findByCompanyName(anyString())).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(CustomerAlreadyExistsException.class)
                .hasMessageContaining("Test Cafe");

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findByIdWithAllRelations(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(response);

        CustomerResponse result = customerService.getCustomerById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getContacts()).isNotNull();
    }

    @Test
    void getCustomerById_NotFound_ThrowsException() {
        when(customerRepository.findByIdWithAllRelations(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(1L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void patchCustomer_UpdateContacts_Success() {
        // Given
        List<CustomerContactDTO> newContacts = new ArrayList<>();
        newContacts.add(CustomerContactDTO.builder()
                .name("Ayşe")
                .surname("Kaya")
                .email("ayse@test.com")
                .phone("5559876543")
                .priority(1)
                .build());
        newContacts.add(CustomerContactDTO.builder()
                .name("Mehmet")
                .surname("Demir")
                .email("mehmet@test.com")
                .phone("5558765432")
                .priority(2)
                .build());

        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .contacts(newContacts)
                .build();

        when(customerRepository.findByIdWithAllRelations(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponse(any(Customer.class))).thenReturn(response);

        // When
        CustomerResponse result = customerService.patchCustomer(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void softDeleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(fileStorageService).moveCustomerFolderToDeleted(anyString());

        customerService.softDeleteCustomer(1L);

        verify(fileStorageService, times(1)).moveCustomerFolderToDeleted("Test Cafe");
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void softDeleteCustomer_NotFound_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.softDeleteCustomer(1L))
                .isInstanceOf(CustomerNotFoundException.class);

        verify(fileStorageService, never()).moveCustomerFolderToDeleted(anyString());
    }

}