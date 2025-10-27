package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CustomerServiceClientFallback implements CustomerServiceClient {

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.warn("⚠️ CustomerService unavailable - Fallback: getAllCustomers");
        return new ArrayList<>();
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        log.warn("⚠️ CustomerService unavailable - Fallback: getCustomerById({})", id);
        return CustomerDTO.builder()
                .id(id)
                .companyName("Unknown Customer")
                .status("UNKNOWN")
                .build();
    }

    @Override
    public List<CustomerDTO> getCustomersByStatus(String status) {
        log.warn("⚠️ CustomerService unavailable - Fallback: getCustomersByStatus({})", status);
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDTO> getActiveCustomers() {
        log.warn("⚠️ CustomerService unavailable - Fallback: getActiveCustomers");
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDTO> getCustomersBySector(String sector) {
        log.warn("⚠️ CustomerService unavailable - Fallback: getCustomersBySector({})", sector);
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDTO> getCustomersWithSpecialDayPostsEnabled() {
        log.warn("⚠️ CustomerService unavailable - Fallback: getCustomersWithSpecialDayPostsEnabled");
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDTO.SpecialDayPreferenceDTO> getCustomerSpecialDayPreferences(Long customerId) {
        log.warn("⚠️ CustomerService unavailable - Fallback: getCustomerSpecialDayPreferences({})", customerId);
        return new ArrayList<>();
    }
}