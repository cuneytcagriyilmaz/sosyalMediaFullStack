package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.request.MockPostRequestDTO;
import com.sosyalmedia.notificationservice.dto.response.MockScheduledPostDTO; // ✅ DÜZELT
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import com.sosyalmedia.notificationservice.service.MockPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
 public class MockPostController {

    private final MockPostService mockPostService;

    /**
     * Post oluştur
     * POST /api/posts
     */
    @PostMapping
    public ResponseEntity<MockScheduledPostDTO> createPost(@RequestBody MockPostRequestDTO request) {
        log.info("📝 Yeni post oluşturuluyor - Customer ID: {}", request.getCustomerId());
        MockScheduledPost post = mockPostService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(post));
    }

    /**
     * Post güncelle
     * PUT /api/posts/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MockScheduledPostDTO> updatePost(
            @PathVariable Long id,
            @RequestBody MockPostRequestDTO request
    ) {
        log.info("📝 Post güncelleniyor - ID: {}", id);
        MockScheduledPost post = mockPostService.updatePost(id, request);
        return ResponseEntity.ok(convertToDTO(post));
    }

    /**
     * Post sil
     * DELETE /api/posts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("🗑️ Post siliniyor - ID: {}", id);
        mockPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Post durumunu güncelle
     * PUT /api/posts/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<MockScheduledPostDTO> updatePostStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        log.info("🔄 Post durumu güncelleniyor - ID: {}, Durum: {}", id, status);
        MockScheduledPost post = mockPostService.updatePostStatus(id, status);
        return ResponseEntity.ok(convertToDTO(post));
    }

    /**
     * Hazırlık durumunu güncelle
     * PUT /api/posts/{id}/preparation-status
     */
    @PutMapping("/{id}/preparation-status")
    public ResponseEntity<MockScheduledPostDTO> updatePreparationStatus(
            @PathVariable Long id,
            @RequestParam String preparationStatus
    ) {
        log.info("🔄 Hazırlık durumu güncelleniyor - ID: {}, Durum: {}", id, preparationStatus);
        MockScheduledPost post = mockPostService.updatePreparationStatus(id, preparationStatus);
        return ResponseEntity.ok(convertToDTO(post));
    }

    /**
     * Post'u yayınla
     * PUT /api/posts/{id}/publish
     */
    @PutMapping("/{id}/publish")
    public ResponseEntity<MockScheduledPostDTO> publishPost(@PathVariable Long id) {
        log.info("📤 Post yayınlanıyor - ID: {}", id);
        MockScheduledPost post = mockPostService.publishPost(id);
        return ResponseEntity.ok(convertToDTO(post));
    }

    /**
     * ID'ye göre post getir
     * GET /api/posts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MockScheduledPostDTO> getPostById(@PathVariable Long id) {
        log.info("🔍 Post getiriliyor - ID: {}", id);
        MockScheduledPost post = mockPostService.getPostById(id);
        return ResponseEntity.ok(convertToDTO(post));
    }

    /**
     * Tüm postları getir (sayfalı)
     * GET /api/posts
     */
    @GetMapping
    public ResponseEntity<Page<MockScheduledPostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "scheduledDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        log.info("📋 Tüm postlar getiriliyor - Page: {}, Size: {}", page, size);

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MockScheduledPost> posts = mockPostService.getAllPosts(pageable);

        return ResponseEntity.ok(posts.map(this::convertToDTO));
    }

    /**
     * Müşteriye göre postları getir
     * GET /api/posts/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<MockScheduledPostDTO>> getCustomerPosts(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("📋 Müşteri postları getiriliyor - Customer ID: {}", customerId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledDate").ascending());
        Page<MockScheduledPost> posts = mockPostService.getCustomerPosts(customerId, pageable);

        return ResponseEntity.ok(posts.map(this::convertToDTO));
    }

    /**
     * Tarih aralığındaki postları getir
     * GET /api/posts/date-range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<MockScheduledPostDTO>> getPostsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        log.info("📋 Tarih aralığındaki postlar getiriliyor: {} - {}", startDate, endDate);
        List<MockScheduledPost> posts = mockPostService.getPostsByDateRange(startDate, endDate);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Duruma göre postları getir
     * GET /api/posts/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MockScheduledPostDTO>> getPostsByStatus(@PathVariable String status) {
        log.info("📋 Duruma göre postlar getiriliyor - Status: {}", status);
        List<MockScheduledPost> posts = mockPostService.getPostsByStatus(status);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Hazırlık durumuna göre postları getir
     * GET /api/posts/preparation-status/{preparationStatus}
     */
    @GetMapping("/preparation-status/{preparationStatus}")
    public ResponseEntity<List<MockScheduledPostDTO>> getPostsByPreparationStatus(
            @PathVariable String preparationStatus
    ) {
        log.info("📋 Hazırlık durumuna göre postlar - Status: {}", preparationStatus);
        List<MockScheduledPost> posts = mockPostService.getPostsByPreparationStatus(preparationStatus);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Yaklaşan postları getir
     * GET /api/posts/upcoming
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<MockScheduledPostDTO>> getUpcomingPosts(
            @RequestParam(defaultValue = "7") int daysAhead
    ) {
        log.info("📋 Yaklaşan postlar getiriliyor - {} gün", daysAhead);
        List<MockScheduledPost> posts = mockPostService.getUpcomingPosts(daysAhead);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Müşterinin yaklaşan postları
     * GET /api/posts/upcoming/customer/{customerId}
     */
    @GetMapping("/upcoming/customer/{customerId}")
    public ResponseEntity<List<MockScheduledPostDTO>> getCustomerUpcomingPosts(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "7") int daysAhead
    ) {
        log.info("📋 Müşterinin yaklaşan postları - Customer ID: {}, {} gün", customerId, daysAhead);
        List<MockScheduledPost> posts = mockPostService.getCustomerUpcomingPosts(customerId, daysAhead);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Gecikmiş postları getir
     * GET /api/posts/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<MockScheduledPostDTO>> getOverduePosts() {
        log.info("📋 Gecikmiş postlar getiriliyor");
        List<MockScheduledPost> posts = mockPostService.getOverduePosts();
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Müşterinin gecikmiş postları
     * GET /api/posts/overdue/customer/{customerId}
     */
    @GetMapping("/overdue/customer/{customerId}")
    public ResponseEntity<List<MockScheduledPostDTO>> getCustomerOverduePosts(
            @PathVariable Long customerId
    ) {
        log.info("📋 Müşterinin gecikmiş postları - Customer ID: {}", customerId);
        List<MockScheduledPost> posts = mockPostService.getCustomerOverduePosts(customerId);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Bugünün postları
     * GET /api/posts/today
     */
    @GetMapping("/today")
    public ResponseEntity<List<MockScheduledPostDTO>> getTodayPosts() {
        log.info("📋 Bugünün postları getiriliyor");
        List<MockScheduledPost> posts = mockPostService.getTodayPosts();
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Müşterinin bugünkü postları
     * GET /api/posts/today/customer/{customerId}
     */
    @GetMapping("/today/customer/{customerId}")
    public ResponseEntity<List<MockScheduledPostDTO>> getCustomerTodayPosts(
            @PathVariable Long customerId
    ) {
        log.info("📋 Müşterinin bugünkü postları - Customer ID: {}", customerId);
        List<MockScheduledPost> posts = mockPostService.getCustomerTodayPosts(customerId);
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Bu haftanın postları
     * GET /api/posts/this-week
     */
    @GetMapping("/this-week")
    public ResponseEntity<List<MockScheduledPostDTO>> getThisWeekPosts() {
        log.info("📋 Bu haftanın postları getiriliyor");
        List<MockScheduledPost> posts = mockPostService.getThisWeekPosts();
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    /**
     * Bu ayın postları
     * GET /api/posts/this-month
     */
    @GetMapping("/this-month")
    public ResponseEntity<List<MockScheduledPostDTO>> getThisMonthPosts() {
        log.info("📋 Bu ayın postları getiriliyor");
        List<MockScheduledPost> posts = mockPostService.getThisMonthPosts();
        return ResponseEntity.ok(convertToDTOs(posts));
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private MockScheduledPostDTO convertToDTO(MockScheduledPost post) {
        long daysRemaining = ChronoUnit.DAYS.between(LocalDateTime.now(), post.getScheduledDate());

        return MockScheduledPostDTO.builder()
                .id(post.getId())
                .customerId(post.getCustomerId())
                .companyName(null) // Customer service'den dolduracaksın
                .scheduledDate(post.getScheduledDate())
                .scheduledTime(post.getScheduledDate().toLocalTime().toString())
                .postType(post.getPostType())
                .isSpecialDayPost(post.getIsSpecialDayPost())
                .specialDateId(post.getSpecialDateId())
                .specialDateName(null) // Special date service'den dolduracaksın
                .specialDateIcon(null)
                .status(post.getStatus())
                .preparationStatus(post.getPreparationStatus())
                .content(post.getContent())
                .platforms(post.getPlatforms())
                .daysRemaining(daysRemaining)
                .priorityColor(determinePriorityColor(daysRemaining, post.getPreparationStatus()))
                .build();
    }

    private List<MockScheduledPostDTO> convertToDTOs(List<MockScheduledPost> posts) {
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private String determinePriorityColor(long daysRemaining, String preparationStatus) {
        if (daysRemaining < 0) return "danger"; // Geçmiş
        if (daysRemaining <= 1) return "danger"; // 1 gün kaldı
        if (daysRemaining <= 3) return "warning"; // 3 gün kaldı
        if (daysRemaining <= 7) return "info"; // 7 gün kaldı
        return "success"; // 7+ gün var
    }
}