package com.sosyalmedia.customerservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosyalmedia.customerservice.dto.CustomerRequest;
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
        validRequest = CustomerRequest.builder()
                .companyName("Integration Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .specialDates(false)  // ← EKLE
                .postType("gorsel")
                .postFrequency("3")
                .postTone("samimi")
                .customerContact1Name("Test")
                .customerContact1Surname("User")
                .customerContact1Email("test@test.com")
                .customerContact1Phone("5551234567")
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
                .andExpect(jsonPath("$.data.id").exists());
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
    void createCustomer_MissingRequiredFields_ReturnsBadRequest() throws Exception {
        CustomerRequest invalidRequest = CustomerRequest.builder()
                .companyName("Test")
                // Missing required fields
                .build();

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
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
                .andExpect(jsonPath("$.data.companyName").value("Integration Test Cafe"));
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
                .andExpect(jsonPath("$.data.id").value(customerId));
    }
}
