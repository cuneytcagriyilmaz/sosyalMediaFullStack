package com.sosyalmedia.notificationservice.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long customerId) {
        super("Customer not found: " + customerId);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}