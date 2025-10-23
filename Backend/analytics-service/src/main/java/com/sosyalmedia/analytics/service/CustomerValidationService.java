// src/main/java/com/sosyalmedia/analytics/service/CustomerValidationService.java

package com.sosyalmedia.analytics.service;

public interface CustomerValidationService {

    /**
     * Müşterinin var olup olmadığını kontrol et
     * Customer-Service'den Feign Client ile sorgular
     *
     * @param customerId Müşteri ID
     * @throws ResourceNotFoundException Müşteri bulunamazsa
     */
    void validateCustomerExists(Long customerId);
}