package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.exception.CustomerNotActiveException;
import com.sosyalmedia.notificationservice.exception.CustomerNotFoundException;
import com.sosyalmedia.notificationservice.exception.CustomerValidationException;
import com.sosyalmedia.notificationservice.service.CustomerValidationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerValidationServiceImpl implements CustomerValidationService {

    private final CustomerServiceClient customerServiceClient;

    @Override
    public CustomerBasicDTO validateAndGetCustomer(Long customerId) {
        log.debug("🔍 Validating customer: {}", customerId);

        try {
            // 1. Customer bilgisini al
            ApiResponse<CustomerBasicDTO> response = customerServiceClient.getCustomerById(customerId);

            // 2. Response kontrolü
            if (!response.isSuccess() || response.getData() == null) {
                log.error("❌ Customer service returned unsuccessful response for ID: {}", customerId);
                throw new CustomerNotFoundException(customerId);
            }

            CustomerBasicDTO customer = response.getData();

            // 3. Customer ID kontrolü (paranoya check)
            if (!customer.getId().equals(customerId)) {
                log.error("❌ Customer ID mismatch! Requested: {}, Received: {}",
                        customerId, customer.getId());
                throw new CustomerValidationException(customerId, "ID mismatch");
            }

            // 4. ACTIVE kontrolü
            if (!"ACTIVE".equals(customer.getStatus())) {
                log.warn("⚠️ Customer is not ACTIVE: {} (Status: {})",
                        customerId, customer.getStatus());
                throw new CustomerNotActiveException(customerId, customer.getStatus());
            }

            // 5. Zorunlu alanlar kontrolü
            if (customer.getCompanyName() == null || customer.getCompanyName().isBlank()) {
                log.error("❌ Customer has no company name: {}", customerId);
                throw new CustomerValidationException(customerId, "Company name is missing");
            }

            log.debug("✅ Customer validated successfully: {} - {}",
                    customerId, customer.getCompanyName());

            return customer;

        } catch (FeignException.NotFound e) {
            log.error("❌ Customer not found: {}", customerId);
            throw new CustomerNotFoundException(customerId);

        } catch (FeignException.ServiceUnavailable e) {
            log.error("❌ Customer service unavailable for customer: {}", customerId);
            throw new CustomerValidationException(customerId, "Customer service unavailable");

        } catch (FeignException e) {
            log.error("❌ Feign error while fetching customer {}: {} (Status: {})",
                    customerId, e.getMessage(), e.status());
            throw new CustomerValidationException(customerId,
                    "Customer service error: " + e.getMessage());
        }
    }

    @Override
    public CustomerBasicDTO getCustomerSafely(Long customerId) {
        try {
            return validateAndGetCustomer(customerId);
        } catch (Exception e) {
            log.warn("⚠️ Could not get customer safely: {} - {}", customerId, e.getMessage());
            // ✅ Olmayan customer için NULL dön, fallback yapma!
            return null;
        }
    }

    @Override
    public boolean customerExists(Long customerId) {
        // ✅ DÜZELTME: Mevcut getCustomerById endpoint'ini kullan
        try {
            ApiResponse<CustomerBasicDTO> response = customerServiceClient.getCustomerById(customerId);
            return response.isSuccess() && response.getData() != null;
        } catch (FeignException.NotFound e) {
            log.debug("🔍 Customer does not exist: {}", customerId);
            return false;
        } catch (Exception e) {
            log.warn("⚠️ Could not check customer existence: {} - {}", customerId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isCustomerActive(Long customerId) {
        try {
            ApiResponse<CustomerBasicDTO> response = customerServiceClient.getCustomerById(customerId);

            if (!response.isSuccess() || response.getData() == null) {
                return false;
            }

            CustomerBasicDTO customer = response.getData();
            return "ACTIVE".equals(customer.getStatus());

        } catch (FeignException.NotFound e) {
            log.debug("🔍 Customer not found: {}", customerId);
            return false;
        } catch (Exception e) {
            log.warn("⚠️ Could not check customer active status: {} - {}", customerId, e.getMessage());
            return false;
        }
    }
}