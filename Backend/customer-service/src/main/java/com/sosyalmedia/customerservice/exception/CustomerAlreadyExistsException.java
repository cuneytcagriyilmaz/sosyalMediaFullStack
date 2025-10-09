package com.sosyalmedia.customerservice.exception;



public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String companyName) {
        super("Bu şirket adı zaten mevcut: " + companyName);
    }
}