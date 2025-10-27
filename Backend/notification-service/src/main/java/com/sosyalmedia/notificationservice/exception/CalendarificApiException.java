package com.sosyalmedia.notificationservice.exception;

public class CalendarificApiException extends RuntimeException {

    public CalendarificApiException(String message) {
        super(message);
    }

    public CalendarificApiException(String message, Throwable cause) {
        super(message, cause);
    }
}