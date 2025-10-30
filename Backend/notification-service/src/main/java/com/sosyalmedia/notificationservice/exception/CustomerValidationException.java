package com.sosyalmedia.notificationservice.exception;

public class CustomerValidationException extends RuntimeException {

    public CustomerValidationException(String message) {
        super(message);
    }

    public CustomerValidationException(Long customerId, String reason) {
        super(String.format("Customer validation failed (ID: %d): %s", customerId, reason));
    }
}