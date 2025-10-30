package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;

public interface CustomerValidationService {

    /**
     * Customer'ı validate et ve bilgilerini getir
     * - Customer var mı?
     * - Customer ACTIVE mi?
     * - Customer bilgileri alınabiliyor mu?
     *

     */
    CustomerBasicDTO validateAndGetCustomer(Long customerId);

    /**
     * Customer bilgisini güvenli şekilde getir
     * Hata olursa exception fırlatmaz, null döner veya varsayılan değer döner
     */
    CustomerBasicDTO getCustomerSafely(Long customerId);

    /**
     * Customer var mı kontrol et (boolean döner)
     */
    boolean customerExists(Long customerId);

    /**
     * Customer ACTIVE mi kontrol et
     */
    boolean isCustomerActive(Long customerId);
}