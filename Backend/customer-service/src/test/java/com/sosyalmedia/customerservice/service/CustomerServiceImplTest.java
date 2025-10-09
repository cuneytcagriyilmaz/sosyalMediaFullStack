package com.sosyalmedia.customerservice.service;

import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.entity.*;
import com.sosyalmedia.customerservice.exception.*;
import com.sosyalmedia.customerservice.mapper.CustomerMapper;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import com.sosyalmedia.customerservice.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest request;
    private Customer customer;
    private CustomerResponse response;

    @BeforeEach
    void setUp() {
        request = CustomerRequest.builder()
                .companyName("Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .postFrequency("2")
                .postType("gorsel")
                .postTone("samimi")
                .customerContact1Name("Test")
                .customerContact1Surname("User")
                .customerContact1Email("test@test.com")
                .customerContact1Phone("5551234567")
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
    }

    @Test
    void getCustomerById_NotFound_ThrowsException() {
        when(customerRepository.findByIdWithAllRelations(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(1L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void softDeleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.softDeleteCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void softDeleteCustomer_NotFound_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.softDeleteCustomer(1L))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}