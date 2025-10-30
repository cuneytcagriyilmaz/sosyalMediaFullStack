package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.client.dto.CustomerFullDTO;
import com.sosyalmedia.notificationservice.client.dto.CustomerSocialMediaDTO;
import com.sosyalmedia.notificationservice.client.dto.CustomerTargetAudienceDTO;
import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.dto.response.AutoScheduleResponse;
import com.sosyalmedia.notificationservice.dto.response.DeadlineSummaryDTO;
import com.sosyalmedia.notificationservice.entity.Platform;
import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.TurkishHoliday;
import com.sosyalmedia.notificationservice.exception.CustomerNotFoundException;
import com.sosyalmedia.notificationservice.repository.PostDeadlineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoScheduleService {

    private final CustomerServiceClient customerServiceClient;
    private final FrequencyCalculatorService frequencyCalculator;
    private final HolidayService holidayService;
    private final PostDeadlineRepository postDeadlineRepository;

    /**
     * 🎯 ANA METHOD: Customer için otomatik takvim oluştur
     *
     * - İlk post (createdAt + 1 ay)
     * - 100 düzenli post (frequency bazlı)
     * - Özel günler (resmi tatiller)
     */
    @Transactional
    public AutoScheduleResponse createAutoSchedule(Long customerId) {
        log.info("🎯 Starting auto-schedule for customer: {}", customerId);

        // 1. Customer bilgilerini al
        CustomerFullDTO customer = getCustomerFullData(customerId);
        CustomerTargetAudienceDTO targetAudience = customer.getTargetAudience();

        // 2. Validation
        validateCustomerForAutoSchedule(customer, targetAudience);

        // 3. Önceki otomatik deadline'ları kontrol et
        Long existingCount = postDeadlineRepository.countAutoCreatedByCustomerId(customerId);
        if (existingCount > 0) {
            log.warn("⚠️ Customer {} already has {} auto-created deadlines. Deleting them first.",
                    customerId, existingCount);
            postDeadlineRepository.deleteAutoCreatedByCustomerId(customerId);
        }

        // 4. Platform'ları belirle
        List<Platform> platforms = determinePlatforms(customer.getSocialMedia());
        log.info("📱 Platforms: {}", platforms);

        // 5. İlk post tarihini hesapla (createdAt + 1 ay)
        LocalDate firstPostDate = customer.getCreatedAt() != null
                ? customer.getCreatedAt().toLocalDate().plusMonths(1)
                : LocalDate.now().plusMonths(1);

        log.info("📅 First post date: {}", firstPostDate);

        // 6. İlk deadline'ı oluştur
        PostDeadline firstPost = createDeadline(
                customer,
                firstPostDate,
                platforms.get(0),
                PostDeadline.EventType.FIRST_POST,
                "🎉 İlk post - " + customer.getCompanyName() + " sosyal medya yolculuğu başlıyor!",
                null,
                null
        );
        postDeadlineRepository.save(firstPost);
        log.info("✅ First post created: {}", firstPostDate);

        // 7. 100 düzenli post oluştur
        Integer frequency = targetAudience.getPostFrequency();  // ✅ DEĞİŞTİ: String -> Integer
        if (frequency == null || frequency < 1 || frequency > 7) {
            log.warn("⚠️ Invalid frequency for customer {}: {}, defaulting to 3", customerId, frequency);
            frequency = 3;
        }

        log.info("📊 Post frequency: {} posts per week", frequency);

        List<LocalDate> regularDates = frequencyCalculator.calculatePostDates(
                firstPostDate,
                frequency,  // ✅ Integer olarak gönder
                100
        );

        List<PostDeadline> regularDeadlines = new ArrayList<>();
        int platformIndex = 0;

        for (LocalDate date : regularDates) {
            Platform platform = platforms.get(platformIndex % platforms.size());

            PostDeadline deadline = createDeadline(
                    customer,
                    date,
                    platform,
                    PostDeadline.EventType.REGULAR,
                    generateRegularPostContent(customer.getCompanyName(), platform),
                    null,
                    null
            );
            regularDeadlines.add(deadline);
            platformIndex++;
        }

        postDeadlineRepository.saveAll(regularDeadlines);
        log.info("✅ Created {} regular deadlines", regularDeadlines.size());

        // 8. Özel günler (resmi tatiller) ekle
        int holidayCount = 0;
        List<PostDeadline> holidayDeadlines = new ArrayList<>();

        if (Boolean.TRUE.equals(targetAudience.getSpecialDates())) {
            log.info("🎊 Processing special dates (holidays)...");

            LocalDate lastRegularDate = regularDates.get(regularDates.size() - 1);
            List<TurkishHoliday> holidays = holidayService.getHolidaysInRange(
                    firstPostDate,
                    lastRegularDate
            );

            log.info("📆 Found {} holidays in date range", holidays.size());

            // Regular dates set'i (duplicate kontrolü için)
            Set<LocalDate> regularDatesSet = new HashSet<>(regularDates);
            regularDatesSet.add(firstPostDate);

            for (TurkishHoliday holiday : holidays) {
                if (!regularDatesSet.contains(holiday.getDate())) {
                    // Yeni holiday deadline oluştur
                    PostDeadline holidayDeadline = createDeadline(
                            customer,
                            holiday.getDate(),
                            platforms.get(0),  // İlk platform
                            PostDeadline.EventType.SPECIAL_DATE,
                            generateHolidayPostContent(customer.getCompanyName(), holiday.getName()),
                            holiday.getName(),
                            holiday.getPrimaryType()
                    );
                    holidayDeadlines.add(holidayDeadline);
                    holidayCount++;
                } else {
                    // Mevcut deadline'ı güncelle (SPECIAL_DATE olarak işaretle)
                    postDeadlineRepository.findByCustomerIdAndScheduledDate(customerId, holiday.getDate())
                            .ifPresent(existing -> {
                                existing.setEventType(PostDeadline.EventType.SPECIAL_DATE);
                                existing.setHolidayName(holiday.getName());
                                existing.setHolidayType(holiday.getPrimaryType());
                                existing.setPostContent(
                                        generateHolidayPostContent(customer.getCompanyName(), holiday.getName())
                                );
                                log.info("🔄 Updated existing deadline as SPECIAL_DATE: {}", holiday.getDate());
                            });
                }
            }

            if (!holidayDeadlines.isEmpty()) {
                postDeadlineRepository.saveAll(holidayDeadlines);
                log.info("✅ Created {} holiday deadlines", holidayDeadlines.size());
            }
        }

        // 9. Response oluştur
        int totalCreated = 1 + regularDeadlines.size() + holidayCount;

        // ✅ Frequency description oluştur
        String frequencyDesc = getFrequencyDescription(frequency);

        AutoScheduleResponse response = AutoScheduleResponse.builder()
                .customerId(customerId)
                .companyName(customer.getCompanyName())
                .firstPostDate(firstPostDate)
                .regularPostsCreated(100)
                .holidayPostsCreated(holidayCount)
                .totalDeadlinesCreated(totalCreated)
                .postFrequency(frequency)  // ✅ Integer
                .postFrequencyDescription(frequencyDesc)  // ✅ YENİ
                .platforms(platforms.stream().map(Enum::name).toList())
                .earliestDeadline(firstPostDate)
                .latestDeadline(regularDates.get(regularDates.size() - 1))
                .sampleDeadlines(createSampleDeadlines(firstPost, regularDeadlines, holidayDeadlines))
                .message(String.format(
                        "✅ %s için %d deadline oluşturuldu (%s, %d düzenli + %d özel gün)",
                        customer.getCompanyName(),
                        totalCreated,
                        frequencyDesc,
                        100,
                        holidayCount
                ))
                .build();

        log.info("🎉 Auto-schedule completed for customer: {}", customerId);
        return response;
    }

    // ========== HELPER METHODS ==========

    /**
     * Customer full data'yı al (targetAudience ve socialMedia ile)
     */
    private CustomerFullDTO getCustomerFullData(Long customerId) {
        try {
            ApiResponse<CustomerFullDTO> response = customerServiceClient.getCustomerFullData(customerId);

            if (!response.isSuccess() || response.getData() == null) {
                log.error("❌ Customer service returned unsuccessful response for ID: {}", customerId);
                throw new CustomerNotFoundException(customerId);
            }

            return response.getData();

        } catch (Exception e) {
            log.error("❌ Failed to get customer full data: {}", customerId, e);
            throw new CustomerNotFoundException(customerId);
        }
    }

    /**
     * Customer ve targetAudience validation
     */
    private void validateCustomerForAutoSchedule(CustomerFullDTO customer, CustomerTargetAudienceDTO targetAudience) {
        if (targetAudience == null) {
            throw new IllegalStateException(
                    "Customer target audience tanımlı değil: " + customer.getId()
            );
        }

        // ✅ DEĞİŞTİ: Integer null kontrolü
        if (targetAudience.getPostFrequency() == null) {
            throw new IllegalStateException(
                    "Customer post frequency tanımlı değil: " + customer.getId()
            );
        }

        // ✅ DEĞİŞTİ: Integer range kontrolü
        Integer freq = targetAudience.getPostFrequency();
        if (freq < 1 || freq > 7) {
            throw new IllegalStateException(
                    "Customer post frequency geçersiz (1-7 arası olmalı): " + customer.getId() + " - " + freq
            );
        }

        log.debug("✅ Customer validation passed for auto-schedule: {}", customer.getId());
    }

    /**
     * Platform'ları belirle (hangi sosyal medya hesapları aktif?)
     */
    private List<Platform> determinePlatforms(CustomerSocialMediaDTO socialMedia) {
        List<Platform> platforms = new ArrayList<>();

        if (socialMedia != null) {
            if (socialMedia.getInstagram() != null && !socialMedia.getInstagram().isBlank()) {
                platforms.add(Platform.INSTAGRAM);
            }
            if (socialMedia.getFacebook() != null && !socialMedia.getFacebook().isBlank()) {
                platforms.add(Platform.FACEBOOK);
            }
            if (socialMedia.getTiktok() != null && !socialMedia.getTiktok().isBlank()) {
                platforms.add(Platform.TIKTOK);
            }
        }

        // Hiçbir platform yoksa default Instagram
        if (platforms.isEmpty()) {
            log.warn("⚠️ No social media platforms found, defaulting to INSTAGRAM");
            platforms.add(Platform.INSTAGRAM);
        }

        return platforms;
    }

    /**
     * Tek bir deadline oluştur
     */
    private PostDeadline createDeadline(
            CustomerFullDTO customer,
            LocalDate scheduledDate,
            Platform platform,
            PostDeadline.EventType eventType,
            String content,
            String holidayName,
            String holidayType) {

        return PostDeadline.builder()
                .customerId(customer.getId())
                .scheduledDate(scheduledDate)
                .status(PostDeadline.PostDeadlineStatus.NOT_STARTED)
                .platform(platform)
                .postContent(content)
                .contentReady(false)
                .eventType(eventType)
                .autoCreated(true)
                .holidayName(holidayName)
                .holidayType(holidayType)
                .build();
    }

    /**
     * Düzenli post içeriği oluştur
     */
    private String generateRegularPostContent(String companyName, Platform platform) {
        return String.format(
                "📱 %s için %s post içeriği hazırlanacak",
                companyName,
                platform.getDisplayName()
        );
    }

    /**
     * Özel gün post içeriği oluştur
     */
    private String generateHolidayPostContent(String companyName, String holidayName) {
        return String.format(
                "🎉 %s - %s özel günü içeriği",
                holidayName,
                companyName
        );
    }

    /**
     * ✅ YENİ: Frequency description oluştur
     */
    private String getFrequencyDescription(Integer frequency) {
        return switch (frequency) {
            case 1 -> "Haftada 1 post (Her Pazartesi)";
            case 2 -> "Haftada 2 post (Pazartesi, Perşembe)";
            case 3 -> "Haftada 3 post (Pazartesi, Çarşamba, Cuma)";
            case 4 -> "Haftada 4 post (Pazartesi, Salı, Perşembe, Cuma)";
            case 5 -> "Haftada 5 post (Her iş günü)";
            case 6 -> "Haftada 6 post (Pazartesi-Cumartesi)";
            case 7 -> "Her gün";
            default -> "Bilinmiyor";
        };
    }

    /**
     * Örnek deadline'ları oluştur (ilk 10)
     */
    private List<DeadlineSummaryDTO> createSampleDeadlines(
            PostDeadline firstPost,
            List<PostDeadline> regular,
            List<PostDeadline> holidays) {

        List<PostDeadline> all = new ArrayList<>();
        all.add(firstPost);
        all.addAll(regular.stream().limit(7).toList());
        all.addAll(holidays.stream().limit(2).toList());

        return all.stream()
                .sorted(Comparator.comparing(PostDeadline::getScheduledDate))
                .limit(10)
                .map(d -> DeadlineSummaryDTO.builder()
                        .date(d.getScheduledDate())
                        .eventType(d.getEventType().name())
                        .eventTypeDisplayName(d.getEventType().getDisplayName())
                        .platform(d.getPlatform().name())
                        .platformDisplayName(d.getPlatform().getDisplayName())
                        .holidayName(d.getHolidayName())
                        .build())
                .collect(Collectors.toList());
    }
}