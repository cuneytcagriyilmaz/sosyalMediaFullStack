// src/main/java/com/sosyalmedia/analytics/service/impl/CustomerAnalyticsServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.client.CustomerServiceClient;
import com.sosyalmedia.analytics.client.dto.customerservice.ApiResponseDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerResponseDTO;
import com.sosyalmedia.analytics.dto.*;
import com.sosyalmedia.analytics.exception.ResourceNotFoundException;
import com.sosyalmedia.analytics.service.ActivityLogService;
import com.sosyalmedia.analytics.service.CustomerAnalyticsService;
import com.sosyalmedia.analytics.service.CustomerNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerAnalyticsServiceImpl implements CustomerAnalyticsService {

    private final ActivityLogService activityLogService;
    private final CustomerNoteService customerNoteService;
    private final CustomerServiceClient customerServiceClient;

    @Override
    public CustomerDetailDTO getCustomerDetail(Long customerId) {
        log.info("Fetching customer detail for ID: {} from customer-service", customerId);

        // ✅ Customer-service'den müşteri bilgisini çek
        ApiResponseDTO<CustomerResponseDTO> response;
        try {
            response = customerServiceClient.getCustomerById(customerId);
        } catch (Exception e) {
            log.error("Failed to fetch customer from customer-service: {}", e.getMessage());
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }

        if (!response.isSuccess() || response.getData() == null) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }

        CustomerResponseDTO customer = response.getData();

        // ✅ Social Media bağlantılarını Map'e çevir
        Map<String, Boolean> socialMediaConnected = new HashMap<>();
        if (customer.getSocialMedia() != null) {
            socialMediaConnected.put("instagram",
                    isNotEmpty(customer.getSocialMedia().getInstagram()));
            socialMediaConnected.put("tiktok",
                    isNotEmpty(customer.getSocialMedia().getTiktok()));
            socialMediaConnected.put("facebook",
                    isNotEmpty(customer.getSocialMedia().getFacebook()));
            socialMediaConnected.put("youtube", false);
        }

        // ✅ Hashtag'leri parse et
        List<String> hashtags = new ArrayList<>();
        if (customer.getTargetAudience() != null &&
                isNotEmpty(customer.getTargetAudience().getCustomerHashtags())) {
            String hashtagStr = customer.getTargetAudience().getCustomerHashtags();
            hashtags = Arrays.stream(hashtagStr.split("[,\\s]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }

        // ✅ Yüklenen medya sayısı
        Integer photosUploaded = customer.getMedia() != null ? customer.getMedia().size() : 0;

        // ✅ İletişim kişisi (priority = 1 olan)
        String contactPerson = null;
        String email = null;
        String phone = null;
        if (customer.getContacts() != null && !customer.getContacts().isEmpty()) {
            var primaryContact = customer.getContacts().stream()
                    .filter(c -> c.getPriority() != null && c.getPriority() == 1)
                    .findFirst();

            if (primaryContact.isPresent()) {
                var contact = primaryContact.get();
                contactPerson = contact.getName() + " " + contact.getSurname();
                email = contact.getEmail();
                phone = contact.getPhone();
            } else {
                var contact = customer.getContacts().get(0);
                contactPerson = contact.getName() + " " + contact.getSurname();
                email = contact.getEmail();
                phone = contact.getPhone();
            }
        }

        // ✅ DTO oluştur
        CustomerDetailDTO detail = CustomerDetailDTO.builder()
                .id(customer.getId())
                .companyName(customer.getCompanyName())
                .sector(customer.getSector())
                .email(email)
                .phone(phone)
                .contactPerson(contactPerson)
                .status(customer.getStatus())
                .membershipPackage(customer.getMembershipPackage())
                .createdAt(customer.getCreatedAt())
                .socialMediaConnected(socialMediaConnected)
                .hashtags(hashtags)
                .photosUploaded(photosUploaded)
                .postStats(getCustomerPostStats(customerId))
                .notes(getCustomerNotes(customerId))
                .build();

        log.info("Customer detail fetched successfully from customer-service");
        return detail;
    }

    @Override
    public PostStatsDTO getCustomerPostStats(Long customerId) {
        log.info("Fetching post stats for customer: {} (MOCK DATA - No post-service yet)", customerId);

        // ✅ POST-SERVICE YOK - Mock data dön
        Map<String, Long> byPlatform = new HashMap<>();
        byPlatform.put("instagram", 35L);
        byPlatform.put("tiktok", 15L);
        byPlatform.put("facebook", 5L);
        byPlatform.put("youtube", 5L);

        return PostStatsDTO.builder()
                .totalGenerated(100L)
                .ready(85L)
                .sent(60L)
                .scheduled(25L)
                .byPlatform(byPlatform)
                .build();
    }

    @Override
    public List<PostDTO> getCustomerUpcomingPosts(Long customerId, int limit) {
        log.info("Fetching {} upcoming posts for customer: {} (MOCK DATA - No post-service yet)",
                limit, customerId);

        // ✅ POST-SERVICE YOK - Mock data dön
        List<PostDTO> posts = new ArrayList<>();

        posts.add(PostDTO.builder()
                .id(101L)
                .title("AI Teknolojileri 2024 Trendi")
                .scheduledDate(LocalDateTime.now().plusDays(1))
                .platform("INSTAGRAM")
                .status("SCHEDULED")
                .thumbnail("https://picsum.photos/200/200?random=1")
                .build());

        posts.add(PostDTO.builder()
                .id(102L)
                .title("Yazılım Geliştirme İpuçları")
                .scheduledDate(LocalDateTime.now().plusDays(2))
                .platform("TIKTOK")
                .status("SCHEDULED")
                .thumbnail("https://picsum.photos/200/200?random=2")
                .build());

        posts.add(PostDTO.builder()
                .id(103L)
                .title("Dijital Dönüşüm Rehberi")
                .scheduledDate(LocalDateTime.now().plusDays(3))
                .platform("FACEBOOK")
                .status("SCHEDULED")
                .thumbnail("https://picsum.photos/200/200?random=3")
                .build());

        return posts.stream().limit(limit).toList();
    }

    @Override
    public List<ActivityLogDTO> getCustomerActivities(Long customerId, int limit) {
        log.info("Fetching {} activities for customer: {}", limit, customerId);
        return activityLogService.getRecentActivitiesByCustomerId(customerId, limit);
    }

    @Override
    public List<CustomerNoteDTO> getCustomerNotes(Long customerId) {
        log.info("Fetching notes for customer: {}", customerId);
        return customerNoteService.getNotesByCustomerId(customerId);
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
