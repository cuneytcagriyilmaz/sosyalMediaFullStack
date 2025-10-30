package com.sosyalmedia.notificationservice.exception;


public class CustomerNotActiveException extends RuntimeException {

    public CustomerNotActiveException(Long customerId, String currentStatus) {
        super(String.format("Müşteri aktif değil (ID: %d, Status: %s). " +
                "Sadece ACTIVE müşterilere deadline eklenebilir.", customerId, currentStatus));
    }
}