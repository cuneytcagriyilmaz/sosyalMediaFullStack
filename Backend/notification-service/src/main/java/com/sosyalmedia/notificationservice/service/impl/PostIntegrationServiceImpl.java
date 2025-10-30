package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.PostServiceClient;
import com.sosyalmedia.notificationservice.client.dto.PostBasicDTO;
import com.sosyalmedia.notificationservice.service.PostIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostIntegrationServiceImpl implements PostIntegrationService {

    private final PostServiceClient postServiceClient;

    @Override
    public PostBasicDTO getPostIfExists(Long postId) {
        try {
            // ⚠️ Post service henüz hazır değil, şimdilik null dön
            log.debug("🔍 Checking post existence: {}", postId);
            // return postServiceClient.getPostById(postId).getData();
            return null; // ⚠️ GEÇICI: Post service hazır olunca açılacak

        } catch (Exception e) {
            log.warn("⚠️ Post service not available yet or post not found: {}", postId);
            return null;
        }
    }

    @Override
    public List<PostBasicDTO> getCustomerPosts(Long customerId) {
        try {
            // ⚠️ Post service henüz hazır değil
            log.debug("🔍 Getting posts for customer: {}", customerId);
            // return postServiceClient.getPostsByCustomer(customerId).getData();
            return Collections.emptyList(); // ⚠️ GEÇICI

        } catch (Exception e) {
            log.warn("⚠️ Post service not available yet for customer: {}", customerId);
            return Collections.emptyList();
        }
    }

    @Override
    public void linkPostToDeadline(Long deadlineId, Long postId) {
        log.info("🔗 Linking post {} to deadline {}", postId, deadlineId);

        // ⚠️ GEÇICI: Post service hazır olunca implement edilecek
        log.warn("⚠️ Post service integration not implemented yet");

        // GELECEK KOD:
        // PostDeadline deadline = repository.findById(deadlineId).orElseThrow();
        // deadline.setPostId(postId);
        // repository.save(deadline);
    }
}