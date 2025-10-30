package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.client.dto.PostBasicDTO;

public interface PostIntegrationService {

    /**
     * Post servis hazır olduğunda kullanılacak
     * Şimdilik mock data döner
     */
    PostBasicDTO getPostIfExists(Long postId);

    /**
     * Customer'ın tüm postlarını getir
     */
    java.util.List<PostBasicDTO> getCustomerPosts(Long customerId);

    /**
     * Post ile deadline'ı ilişkilendir
     */
    void linkPostToDeadline(Long deadlineId, Long postId);
}