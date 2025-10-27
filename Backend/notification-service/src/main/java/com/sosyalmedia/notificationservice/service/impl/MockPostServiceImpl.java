package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import com.sosyalmedia.notificationservice.dto.request.MockPostRequestDTO;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import com.sosyalmedia.notificationservice.repository.GlobalSpecialDateRepository;
import com.sosyalmedia.notificationservice.repository.MockScheduledPostRepository;
import com.sosyalmedia.notificationservice.service.MockPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockPostServiceImpl implements MockPostService {

    private final MockScheduledPostRepository postRepository;
    private final GlobalSpecialDateRepository specialDateRepository;
    private final CustomerServiceClient customerServiceClient;

    // ==================== CREATE ====================

    @Override
    @Transactional
    public List<MockScheduledPost> createPostScheduleForCustomer(Long customerId) {
        log.info("📅 {} numaralı müşteri için post planı oluşturuluyor...", customerId);

        // Müşteri bilgilerini al
        CustomerDTO customer = customerServiceClient.getCustomerById(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }

        // İlk post tarihi = Kayıt tarihi + 30 gün
        LocalDate firstPostDate = customer.getRegistrationDate().plusDays(30);
        String postFrequency = customer.getTargetAudience().getPostFrequency();

        log.info("İlk post tarihi: {}, Post sıklığı: Haftada {} gün", firstPostDate, postFrequency);

        List<MockScheduledPost> allPosts = new ArrayList<>();

        // 1. Normal postları oluştur (100 adet)
        List<MockScheduledPost> normalPosts = generateNormalPosts(
                customerId,
                firstPostDate,
                postFrequency,
                100
        );
        allPosts.addAll(normalPosts);

        // 2. Özel gün postlarını ekle (eğer müşteri aktif ettiyse)
        if (customer.getEnableSpecialDayPosts() != null && customer.getEnableSpecialDayPosts()) {
            log.info("🎉 Özel gün postları ekleniyor...");

            List<MockScheduledPost> specialDayPosts = generateSpecialDayPosts(
                    customerId,
                    customer,
                    firstPostDate,
                    firstPostDate.plusYears(2)
            );
            allPosts.addAll(specialDayPosts);

            log.info("✅ {} özel gün postu eklendi", specialDayPosts.size());
        }

        // Veritabanına kaydet
        List<MockScheduledPost> savedPosts = postRepository.saveAll(allPosts);

        log.info("✅ {} normal + {} özel gün = Toplam {} post planlandı",
                normalPosts.size(),
                allPosts.size() - normalPosts.size(),
                savedPosts.size()
        );

        return savedPosts;
    }

    @Override
    @Transactional
    public MockScheduledPost createPost(MockPostRequestDTO requestDTO) {
        log.info("📝 Yeni post oluşturuluyor - Customer ID: {}", requestDTO.getCustomerId());

        MockScheduledPost post = MockScheduledPost.builder()
                .customerId(requestDTO.getCustomerId())
                .scheduledDate(requestDTO.getScheduledDate())
                .postType(requestDTO.getPostType() != null ? requestDTO.getPostType() : "NORMAL")
                .isSpecialDayPost(requestDTO.getIsSpecialDayPost() != null ? requestDTO.getIsSpecialDayPost() : false)
                .specialDateId(requestDTO.getSpecialDateId())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : "SCHEDULED")
                .preparationStatus(requestDTO.getPreparationStatus() != null ? requestDTO.getPreparationStatus() : "NOT_STARTED")
                .content(requestDTO.getContent())
                .platforms(requestDTO.getPlatforms() != null ? requestDTO.getPlatforms() : "Instagram,Facebook")
                .notes(requestDTO.getNotes())
                .build();

        MockScheduledPost saved = postRepository.save(post);
        log.info("✅ Post oluşturuldu - ID: {}", saved.getId());
        return saved;
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public MockScheduledPost updatePost(Long postId, MockPostRequestDTO requestDTO) {
        MockScheduledPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        log.info("📝 Post güncelleniyor - ID: {}", postId);

        post.setScheduledDate(requestDTO.getScheduledDate());
        post.setPostType(requestDTO.getPostType());
        post.setIsSpecialDayPost(requestDTO.getIsSpecialDayPost());
        post.setSpecialDateId(requestDTO.getSpecialDateId());
        post.setStatus(requestDTO.getStatus());
        post.setPreparationStatus(requestDTO.getPreparationStatus());
        post.setContent(requestDTO.getContent());
        post.setPlatforms(requestDTO.getPlatforms());
        post.setNotes(requestDTO.getNotes());

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public MockScheduledPost updatePostStatus(Long postId, String status) {
        MockScheduledPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setStatus(status);
        log.info("🔄 Post durumu güncellendi - ID: {}, Durum: {}", postId, status);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public MockScheduledPost updatePreparationStatus(Long postId, String preparationStatus) {
        MockScheduledPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setPreparationStatus(preparationStatus);
        log.info("🔄 Hazırlık durumu güncellendi - ID: {}, Durum: {}", postId, preparationStatus);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public MockScheduledPost publishPost(Long postId) {
        MockScheduledPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setStatus("PUBLISHED");
        post.setPublishedAt(LocalDateTime.now());

        log.info("📤 Post yayınlandı - ID: {}", postId);
        return postRepository.save(post);
    }

    // ==================== DELETE ====================

    @Override
    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        postRepository.deleteById(postId);
        log.info("🗑️ Post silindi - ID: {}", postId);
    }

    @Override
    @Transactional
    public void deleteCustomerPosts(Long customerId) {
        postRepository.deleteByCustomerId(customerId);
        log.info("🗑️ {} numaralı müşterinin tüm postları silindi", customerId);
    }

    // ==================== GET - SINGLE & PAGINATED ====================

    @Override
    public MockScheduledPost getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    public Page<MockScheduledPost> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<MockScheduledPost> getCustomerPosts(Long customerId, Pageable pageable) {
        return postRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    public List<MockScheduledPost> getCustomerPostsList(Long customerId) {
        return postRepository.findByCustomerIdOrderByScheduledDateAsc(customerId);
    }

    // ==================== GET - BY FILTERS ====================

    @Override
    public List<MockScheduledPost> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findByScheduledDateBetweenOrderByScheduledDateAsc(startDate, endDate);
    }

    @Override
    public List<MockScheduledPost> getCustomerPostsByDateRange(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return postRepository.findByCustomerIdAndScheduledDateBetween(customerId, startDate, endDate);
    }

    @Override
    public List<MockScheduledPost> getPostsByStatus(String status) {
        return postRepository.findByStatus(status);
    }

    @Override
    public List<MockScheduledPost> getPostsByPreparationStatus(String preparationStatus) {
        return postRepository.findByPreparationStatus(preparationStatus);
    }

    // ==================== GET - TIME-BASED ====================

    @Override
    public List<MockScheduledPost> getUpcomingPosts(int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(daysAhead);
        return postRepository.findUpcomingPosts(now, endDate);
    }

    @Override
    public List<MockScheduledPost> getCustomerUpcomingPosts(Long customerId, int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(daysAhead);
        return postRepository.findUpcomingPostsByCustomer(customerId, now, endDate);
    }

    @Override
    public List<MockScheduledPost> getTodayPosts() {
        return postRepository.findTodayPosts(LocalDate.now());
    }

    @Override
    public List<MockScheduledPost> getCustomerTodayPosts(Long customerId) {
        return postRepository.findTodayPostsByCustomer(customerId, LocalDate.now());
    }

    @Override
    public List<MockScheduledPost> getThisWeekPosts() {
        LocalDateTime startOfWeek = LocalDate.now().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        return postRepository.findByScheduledDateBetweenOrderByScheduledDateAsc(startOfWeek, endOfWeek);
    }

    @Override
    public List<MockScheduledPost> getThisMonthPosts() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusDays(1).withHour(23).withMinute(59);
        return postRepository.findByScheduledDateBetweenOrderByScheduledDateAsc(startOfMonth, endOfMonth);
    }

    // ==================== GET - SPECIAL CASES ====================

    @Override
    public List<MockScheduledPost> getOverduePosts() {
        return postRepository.findOverduePosts(LocalDateTime.now());
    }

    @Override
    public List<MockScheduledPost> getCustomerOverduePosts(Long customerId) {
        return postRepository.findOverduePostsByCustomer(customerId, LocalDateTime.now());
    }

    @Override
    public List<MockScheduledPost> getCriticalPosts(int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(daysAhead);
        return postRepository.findCriticalPosts(now, endDate);
    }

    @Override
    public List<MockScheduledPost> getSpecialDayPosts() {
        return postRepository.findByIsSpecialDayPostTrue();
    }

    @Override
    public List<MockScheduledPost> getNormalPosts() {
        return postRepository.findByIsSpecialDayPostFalse();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Normal postları oluştur
     */
    private List<MockScheduledPost> generateNormalPosts(
            Long customerId,
            LocalDate startDate,
            String postFrequency,
            int totalPosts
    ) {
        List<MockScheduledPost> posts = new ArrayList<>();
        LocalDate currentDate = startDate;
        int postsCreated = 0;

        while (postsCreated < totalPosts) {
            if (shouldCreatePostOnDate(currentDate, postFrequency)) {
                MockScheduledPost post = MockScheduledPost.builder()
                        .customerId(customerId)
                        .scheduledDate(currentDate.atTime(9, 0))
                        .postType("NORMAL")
                        .isSpecialDayPost(false)
                        .specialDateId(null)
                        .status("SCHEDULED")
                        .preparationStatus("NOT_STARTED")
                        .platforms("Instagram,Facebook")
                        .build();

                posts.add(post);
                postsCreated++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return posts;
    }

    /**
     * Özel gün postlarını oluştur
     */
    private List<MockScheduledPost> generateSpecialDayPosts(
            Long customerId,
            CustomerDTO customer,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<MockScheduledPost> specialPosts = new ArrayList<>();

        List<CustomerDTO.SpecialDayPreferenceDTO> preferences = customer.getSpecialDayPreferences();

        if (preferences == null || preferences.isEmpty()) {
            log.debug("Müşterinin seçili özel günü yok");
            return specialPosts;
        }

        List<GlobalSpecialDate> specialDates = specialDateRepository
                .findByDateValueBetweenOrderByDateValueAsc(startDate, endDate);

        for (GlobalSpecialDate specialDate : specialDates) {
            boolean isSelected = preferences.stream()
                    .anyMatch(pref -> pref.getSpecialDateId().equals(specialDate.getId())
                            && pref.getIsEnabled());

            if (isSelected) {
                LocalTime customTime = preferences.stream()
                        .filter(pref -> pref.getSpecialDateId().equals(specialDate.getId()))
                        .map(pref -> LocalTime.parse(pref.getCustomTime()))
                        .findFirst()
                        .orElse(LocalTime.of(14, 0));

                MockScheduledPost specialPost = MockScheduledPost.builder()
                        .customerId(customerId)
                        .scheduledDate(specialDate.getDateValue().atTime(customTime))
                        .postType("SPECIAL_DAY")
                        .isSpecialDayPost(true)
                        .specialDateId(specialDate.getId())
                        .status("SCHEDULED")
                        .preparationStatus("NOT_STARTED")
                        .content("Özel Gün: " + specialDate.getDateName())
                        .platforms("Instagram,Facebook")
                        .build();

                specialPosts.add(specialPost);

                log.debug("🎉 Özel gün postu eklendi: {} - {}",
                        specialDate.getDateName(),
                        specialDate.getDateValue()
                );
            }
        }

        return specialPosts;
    }

    /**
     * Bu tarihte post oluşturulmalı mı?
     */
    private boolean shouldCreatePostOnDate(LocalDate date, String frequency) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return switch (frequency) {
            case "3" -> dayOfWeek == DayOfWeek.MONDAY ||
                    dayOfWeek == DayOfWeek.WEDNESDAY ||
                    dayOfWeek == DayOfWeek.FRIDAY;
            case "5" -> dayOfWeek.getValue() <= 5;
            case "7" -> true;
            default -> dayOfWeek == DayOfWeek.MONDAY;
        };
    }
}