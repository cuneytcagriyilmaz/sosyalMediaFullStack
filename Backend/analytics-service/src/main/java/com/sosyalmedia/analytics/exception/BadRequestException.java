// src/main/java/com/sosyalmedia/analytics/exception/BadRequestException.java

package com.sosyalmedia.analytics.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}