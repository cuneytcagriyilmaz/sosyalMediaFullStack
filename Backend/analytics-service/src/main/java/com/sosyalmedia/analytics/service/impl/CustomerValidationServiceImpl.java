// src/main/java/com/sosyalmedia/analytics/service/impl/CustomerValidationServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.client.CustomerServiceClient;
import com.sosyalmedia.analytics.client.dto.customerservice.ApiResponseDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerResponseDTO;
import com.sosyalmedia.analytics.exception.ResourceNotFoundException;
import com.sosyalmedia.analytics.service.CustomerValidationService;
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
    public void validateCustomerExists(Long customerId) {
        log.debug("🔍 Validating customer exists: {}", customerId);

        try {
            ApiResponseDTO<CustomerResponseDTO> response = customerServiceClient.getCustomerById(customerId);

            if (!response.isSuccess() || response.getData() == null) {
                log.warn("❌ Customer not found: {}", customerId);
                throw new ResourceNotFoundException("Customer", "id", customerId);
            }

            log.debug("✅ Customer validation successful: {} - {}",
                    customerId, response.getData().getCompanyName());

        } catch (FeignException.NotFound e) {
            log.warn("❌ Customer not found (404): {}", customerId);
            throw new ResourceNotFoundException("Customer", "id", customerId);

        } catch (FeignException e) {
            log.error("❌ Feign error while validating customer {}: {} - {}",
                    customerId, e.status(), e.getMessage());
            throw new ResourceNotFoundException("Customer", "id", customerId);

        } catch (ResourceNotFoundException e) {
            // Already a ResourceNotFoundException, just re-throw
            throw e;

        } catch (Exception e) {
            log.error("❌ Unexpected error while validating customer {}: {}", customerId, e.getMessage(), e);
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }
    }
}
