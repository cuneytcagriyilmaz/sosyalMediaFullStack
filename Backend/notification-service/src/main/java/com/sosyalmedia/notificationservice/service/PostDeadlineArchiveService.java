package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.response.PostDeadlineArchiveResponse;

import java.util.List;

public interface PostDeadlineArchiveService {

    /**
     * Deadline'ı arşivle ve orijinalini sil
     */
    PostDeadlineArchiveResponse archiveDeadline(Long deadlineId, String reason);

    /**
     * Tüm arşivlenmiş deadline'ları getir
     */
    List<PostDeadlineArchiveResponse> getAllArchivedDeadlines();

    /**
     * Customer'a göre arşiv
     */
    List<PostDeadlineArchiveResponse> getArchivedDeadlinesByCustomer(Long customerId);

    /**
     * Arşivden geri yükle
     */
    void restoreDeadline(Long archiveId);

    /**
     * Arşivi kalıcı sil
     */
    void deleteArchive(Long archiveId);

    /**
     * Arşiv istatistikleri
     */
    long getTotalArchivedCount();

    long getArchivedCountLast30Days();
}