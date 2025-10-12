package com.sosyalmedia.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosyalmedia.customerservice.dto.*;
import com.sosyalmedia.customerservice.entity.Customer;
import com.sosyalmedia.customerservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void createCustomer_ValidRequest_ReturnsCreated() throws Exception {
        // ContactDTO listesi
        List<ContactDTO> contacts = new ArrayList<>();
        contacts.add(ContactDTO.builder()
                .name("Test")
                .surname("User")
                .email("test@test.com")
                .phone("5551234567")
                .priority(1)
                .build());

        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .specialDates(false)
                .contacts(contacts)  // YENİ: Liste olarak
                .postFrequency("2")
                .postType("gorsel")
                .postTone("samimi")
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .companyName("Test Cafe")
                .contacts(contacts)
                .build();

        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyName").value("Test Cafe"))
                .andExpect(jsonPath("$.data.contacts[0].name").value("Test"));
    }

    @Test
    void createCustomer_InvalidRequest_ReturnsBadRequest() throws Exception {
        CustomerRequest request = CustomerRequest.builder().build(); // Boş request

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomer_NoContacts_ReturnsBadRequest() throws Exception {
        CustomerRequest request = CustomerRequest.builder()
                .companyName("Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .specialDates(false)
                .contacts(new ArrayList<>())  // Boş contact listesi
                .postFrequency("2")
                .postType("gorsel")
                .postTone("samimi")
                .build();

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerById_Exists_ReturnsOk() throws Exception {
        List<ContactDTO> contacts = new ArrayList<>();
        contacts.add(ContactDTO.builder()
                .name("Test")
                .surname("User")
                .email("test@test.com")
                .phone("5551234567")
                .priority(1)
                .build());

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .companyName("Test Cafe")
                .contacts(contacts)
                .build();

        when(customerService.getCustomerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.contacts[0].name").value("Test"));
    }

    @Test
    void getAllCustomers_ReturnsOk() throws Exception {
        CustomerListResponse customer = CustomerListResponse.builder()
                .id(1L)
                .companyName("Test Cafe")
                .build();

        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].companyName").value("Test Cafe"));
    }

    @Test
    void deleteCustomer_ReturnsOk() throws Exception {
        doNothing().when(customerService).softDeleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void patchCustomer_UpdateContacts_ReturnsOk() throws Exception {
        // YENİ: Contact güncelleme testi
        List<ContactDTO> newContacts = new ArrayList<>();
        newContacts.add(ContactDTO.builder()
                .name("Ayşe")
                .surname("Kaya")
                .email("ayse@test.com")
                .phone("5559876543")
                .priority(1)
                .build());

        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .contacts(newContacts)
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .companyName("Test Cafe")
                .contacts(newContacts)
                .build();

        when(customerService.patchCustomer(eq(1L), any(CustomerUpdateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.contacts[0].name").value("Ayşe"));
    }
}