package com.sosyalmedia.notificationservice.exception;

public class PostDeadlineNotFoundException extends RuntimeException {

    public PostDeadlineNotFoundException(Long id) {
        super(String.format("Post deadline bulunamadı: %d", id));
    }
}