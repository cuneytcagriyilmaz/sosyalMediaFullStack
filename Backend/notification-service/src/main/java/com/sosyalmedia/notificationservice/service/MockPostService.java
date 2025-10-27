package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.MockPostRequestDTO;
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MockPostService {

    // ==================== CREATE ====================

    /**
     * Müşteri için otomatik post planı oluştur (100 post + özel günler)
     */
    List<MockScheduledPost> createPostScheduleForCustomer(Long customerId);

    /**
     * Tek bir post oluştur
     */
    MockScheduledPost createPost(MockPostRequestDTO requestDTO);

    // ==================== UPDATE ====================

    /**
     * Post güncelle
     */
    MockScheduledPost updatePost(Long postId, MockPostRequestDTO requestDTO);

    /**
     * Post durumunu güncelle
     */
    MockScheduledPost updatePostStatus(Long postId, String status);

    /**
     * Hazırlık durumunu güncelle
     */
    MockScheduledPost updatePreparationStatus(Long postId, String preparationStatus);

    /**
     * Postu yayınla
     */
    MockScheduledPost publishPost(Long postId);

    // ==================== DELETE ====================

    /**
     * Post sil
     */
    void deletePost(Long postId);

    /**
     * Müşterinin tüm postlarını sil
     */
    void deleteCustomerPosts(Long customerId);

    // ==================== GET - SINGLE & PAGINATED ====================

    /**
     * ID'ye göre post getir
     */
    MockScheduledPost getPostById(Long postId);

    /**
     * Tüm postları getir (sayfalı)
     */
    Page<MockScheduledPost> getAllPosts(Pageable pageable);

    /**
     * Müşterinin postları (sayfalı)
     */
    Page<MockScheduledPost> getCustomerPosts(Long customerId, Pageable pageable);

    /**
     * Müşterinin tüm postları (liste)
     */
    List<MockScheduledPost> getCustomerPostsList(Long customerId);

    // ==================== GET - BY FILTERS ====================

    /**
     * Tarih aralığındaki postları getir
     */
    List<MockScheduledPost> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Müşteri ve tarih aralığına göre postları getir
     */
    List<MockScheduledPost> getCustomerPostsByDateRange(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Duruma göre postları getir
     */
    List<MockScheduledPost> getPostsByStatus(String status);

    /**
     * Hazırlık durumuna göre postları getir
     */
    List<MockScheduledPost> getPostsByPreparationStatus(String preparationStatus);

    // ==================== GET - TIME-BASED ====================

    /**
     * Yaklaşan postları getir (N gün içinde)
     */
    List<MockScheduledPost> getUpcomingPosts(int daysAhead);

    /**
     * Müşterinin yaklaşan postları
     */
    List<MockScheduledPost> getCustomerUpcomingPosts(Long customerId, int daysAhead);

    /**
     * Bugünün postları
     */
    List<MockScheduledPost> getTodayPosts();

    /**
     * Müşterinin bugünkü postları
     */
    List<MockScheduledPost> getCustomerTodayPosts(Long customerId);

    /**
     * Bu haftanın postları
     */
    List<MockScheduledPost> getThisWeekPosts();

    /**
     * Bu ayın postları
     */
    List<MockScheduledPost> getThisMonthPosts();

    // ==================== GET - SPECIAL CASES ====================

    /**
     * Gecikmiş postları getir
     */
    List<MockScheduledPost> getOverduePosts();

    /**
     * Müşterinin gecikmiş postları
     */
    List<MockScheduledPost> getCustomerOverduePosts(Long customerId);

    /**
     * Kritik postları getir (yaklaşan ve hazır olmayan)
     */
    List<MockScheduledPost> getCriticalPosts(int daysAhead);

    /**
     * Özel gün postlarını getir
     */
    List<MockScheduledPost> getSpecialDayPosts();

    /**
     * Normal postları getir
     */
    List<MockScheduledPost> getNormalPosts();
}