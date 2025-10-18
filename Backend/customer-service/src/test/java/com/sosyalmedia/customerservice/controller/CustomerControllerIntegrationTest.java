package com.sosyalmedia.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosyalmedia.customerservice.dto.CustomerContactDTO;
import com.sosyalmedia.customerservice.dto.request.CustomerRequest;
import com.sosyalmedia.customerservice.entity.Customer;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerRequest validRequest;

    @BeforeEach
    void setUp() {
        // ContactDTO listesi
        List<CustomerContactDTO> contacts = new ArrayList<>();
        contacts.add(CustomerContactDTO.builder()
                .name("Ahmet")
                .surname("Yılmaz")
                .email("ahmet@test.com")
                .phone("5551234567")
                .priority(1)
                .build());

        validRequest = CustomerRequest.builder()
                .companyName("Integration Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .specialDates(false)
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .contacts(contacts)  // YENİ: Liste olarak
                .build();
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void createCustomer_Success() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyName").value("Integration Test Cafe"))
                .andExpect(jsonPath("$.data.sector").value("cafe"))
                .andExpect(jsonPath("$.data.contacts").isArray())
                .andExpect(jsonPath("$.data.contacts[0].name").value("Ahmet"))
                .andExpect(jsonPath("$.data.contacts[0].email").value("ahmet@test.com"))
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void createCustomer_WithMultipleContacts_Success() throws Exception {
        // Given - 3 contact
        List<CustomerContactDTO> contacts = new ArrayList<>();
        contacts.add(CustomerContactDTO.builder()
                .name("Ahmet")
                .surname("Yılmaz")
                .email("ahmet@test.com")
                .phone("5551234567")
                .priority(1)
                .build());
        contacts.add(CustomerContactDTO.builder()
                .name("Ayşe")
                .surname("Kaya")
                .email("ayse@test.com")
                .phone("5559876543")
                .priority(2)
                .build());
        contacts.add(CustomerContactDTO.builder()
                .name("Mehmet")
                .surname("Demir")
                .email("mehmet@test.com")
                .phone("5558765432")
                .priority(3)
                .build());

        validRequest.setContacts(contacts);

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.contacts").isArray())
                .andExpect(jsonPath("$.data.contacts", hasSize(3)))
                .andExpect(jsonPath("$.data.contacts[0].name").value("Ahmet"))
                .andExpect(jsonPath("$.data.contacts[1].name").value("Ayşe"))
                .andExpect(jsonPath("$.data.contacts[2].name").value("Mehmet"));
    }

    @Test
    void createCustomer_DuplicateName_ReturnsConflict() throws Exception {
        // First creation
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

        // Duplicate creation
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    void createCustomer_NoContacts_ReturnsBadRequest() throws Exception {
        validRequest.setContacts(new ArrayList<>());  // Boş liste

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void getAllCustomers_ReturnsCustomerList() throws Exception {
        // Create test customer
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));

        // Get all
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data[0].companyName").exists());
    }

    @Test
    void getCustomerById_Success() throws Exception {
        // Create customer
        String response = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long customerId = objectMapper.readTree(response).get("data").get("id").asLong();

        // Get by ID
        mockMvc.perform(get("/api/customers/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(customerId))
                .andExpect(jsonPath("$.data.companyName").value("Integration Test Cafe"))
                .andExpect(jsonPath("$.data.contacts").isArray())
                .andExpect(jsonPath("$.data.contacts[0].name").value("Ahmet"));
    }

    @Test
    void getCustomerById_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getCustomersBySector_ReturnsFilteredList() throws Exception {
        // Create customer
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));

        // Filter by sector
        mockMvc.perform(get("/api/customers/sector/cafe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data[0].sector").value("cafe"));
    }

    @Test
    void softDeleteCustomer_Success() throws Exception {
        // Create customer
        String response = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long customerId = objectMapper.readTree(response).get("data").get("id").asLong();

        // Soft delete
        mockMvc.perform(delete("/api/customers/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Verify it's in deleted list
        mockMvc.perform(get("/api/customers/deleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void restoreCustomer_Success() throws Exception {
        // Create and soft delete
        String response = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long customerId = objectMapper.readTree(response).get("data").get("id").asLong();

        mockMvc.perform(delete("/api/customers/" + customerId))
                .andExpect(status().isOk());

        // Restore
        mockMvc.perform(put("/api/customers/" + customerId + "/restore"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Verify it's back in active list
        mockMvc.perform(get("/api/customers/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(customerId))
                .andExpect(jsonPath("$.data.contacts").isArray());
    }
}