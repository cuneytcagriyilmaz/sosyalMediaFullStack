package com.sosyalmedia.notificationservice.exception;


public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long customerId) {
        super(String.format("Müşteri bulunamadı: %d", customerId));
    }
}