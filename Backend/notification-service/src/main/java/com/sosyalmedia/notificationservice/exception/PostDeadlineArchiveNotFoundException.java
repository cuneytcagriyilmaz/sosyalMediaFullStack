package com.sosyalmedia.notificationservice.exception;

public class PostDeadlineArchiveNotFoundException extends RuntimeException {

    public PostDeadlineArchiveNotFoundException(Long archiveId) {
        super(String.format("Arşiv bulunamadı: %d", archiveId));
    }
}