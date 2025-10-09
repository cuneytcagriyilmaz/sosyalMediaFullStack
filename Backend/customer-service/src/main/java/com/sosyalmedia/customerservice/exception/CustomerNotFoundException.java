package com.sosyalmedia.customerservice.exception;


public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(Long id) {
        super("Müşteri bulunamadı: " + id);
    }

    public CustomerNotFoundException(String field, String value) {
        super("Müşteri bulunamadı: " + field + " = " + value);
    }
}