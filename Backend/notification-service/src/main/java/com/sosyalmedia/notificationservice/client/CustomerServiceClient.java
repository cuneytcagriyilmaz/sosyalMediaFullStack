package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "customer-service",
        fallback = CustomerServiceClientFallback.class
)
public interface CustomerServiceClient {

    /**
     * Tüm müşterileri getir
     */
    @GetMapping("/api/customers")
    List<CustomerDTO> getAllCustomers();

    /**
     * ID'ye göre müşteri getir
     */
    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") Long id);

    /**
     * Duruma göre müşterileri getir
     */
    @GetMapping("/api/customers/status/{status}")
    List<CustomerDTO> getCustomersByStatus(@PathVariable("status") String status);

    /**
     * Aktif müşterileri getir
     */
    @GetMapping("/api/customers/active")
    List<CustomerDTO> getActiveCustomers();

    /**
     * Sektöre göre müşterileri getir
     */
    @GetMapping("/api/customers/sector/{sector}")
    List<CustomerDTO> getCustomersBySector(@PathVariable("sector") String sector);

    /**
     * Özel gün ayarı açık olan müşterileri getir
     */
    @GetMapping("/api/customers/special-day-posts-enabled")
    List<CustomerDTO> getCustomersWithSpecialDayPostsEnabled();

    /**
     * Müşterinin özel gün tercihlerini getir
     */
    @GetMapping("/api/customers/{id}/special-day-preferences")
    List<CustomerDTO.SpecialDayPreferenceDTO> getCustomerSpecialDayPreferences(
            @PathVariable("id") Long customerId
    );
}