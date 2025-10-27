package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.external.CalendarificResponseDTO;
import com.sosyalmedia.notificationservice.exception.CalendarificApiException;
import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;
import com.sosyalmedia.notificationservice.repository.GlobalSpecialDateRepository;
import com.sosyalmedia.notificationservice.service.CalendarificService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarificServiceImpl implements CalendarificService {

    @Value("${calendarific.api-key}")
    private String apiKey;

    @Value("${calendarific.base-url}")
    private String baseUrl;

    @Value("${calendarific.country}")
    private String country;

    private final RestTemplate restTemplate;
    private final GlobalSpecialDateRepository globalDateRepository;

    @Override
    @Transactional
    public void syncHolidays(int year) {
        try {
            log.info("🔄 Calendarific API'den {} yılı tatilleri çekiliyor...", year);

            String url = String.format(
                    "%s/holidays?api_key=%s&country=%s&year=%d",
                    baseUrl, apiKey, country, year
            );

            CalendarificResponseDTO response = restTemplate.getForObject(url, CalendarificResponseDTO.class);

            if (response == null || response.getResponse() == null) {
                throw new CalendarificApiException("Calendarific API'den veri alınamadı");
            }

            int newCount = 0;
            int updatedCount = 0;

            for (CalendarificResponseDTO.Holiday holiday : response.getResponse().getHolidays()) {
                // Sadece önemli tatilleri al
                if (!isImportantHoliday(holiday.getType())) {
                    continue;
                }

                LocalDate holidayDate = LocalDate.of(
                        holiday.getDate().getDateTime().getYear(),
                        holiday.getDate().getDateTime().getMonth(),
                        holiday.getDate().getDateTime().getDay()
                );

                Optional<GlobalSpecialDate> existing = globalDateRepository
                        .findByDateNameAndDateValue(holiday.getName(), holidayDate);

                if (existing.isEmpty()) {
                    GlobalSpecialDate specialDate = GlobalSpecialDate.builder()
                            .dateType(determineHolidayType(holiday.getType()))
                            .dateName(holiday.getName())
                            .dateValue(holidayDate)
                            .description(holiday.getDescription())
                            .icon(getIconForHoliday(holiday.getName()))
                            .isRecurring(true)
                            .autoSuggestPost(true)
                            .applicableSectors(null)
                            .build();

                    globalDateRepository.save(specialDate);
                    newCount++;
                    log.debug("➕ Yeni tatil: {}", holiday.getName());
                } else {
                    updatedCount++;
                }
            }

            log.info("✅ {} yeni tatil eklendi, {} mevcut tatil", newCount, updatedCount);

        } catch (Exception e) {
            log.error("❌ Calendarific API hatası: {}", e.getMessage(), e);
            throw new CalendarificApiException("Tatil senkronizasyonu başarısız: " + e.getMessage(), e);
        }
    }

    @Override
    @Scheduled(cron = "0 0 2 1 1 ?") // Her yıl 1 Ocak 02:00
    public void autoSyncHolidays() {
        int currentYear = LocalDate.now().getYear();
        log.info("⏰ Otomatik tatil senkronizasyonu başlatıldı: {}", currentYear);

        syncHolidays(currentYear);
        syncHolidays(currentYear + 1); // Gelecek yıl için
    }

    @Override
    @PostConstruct
    public void initSync() {
        int currentYear = LocalDate.now().getYear();

        LocalDate startDate = LocalDate.of(currentYear, 1, 1);
        LocalDate endDate = LocalDate.of(currentYear, 12, 31);

        long count = globalDateRepository.countByDateValueBetween(startDate, endDate);

        if (count == 0) {
            log.info("📅 İlk senkronizasyon başlatılıyor: {} yılı", currentYear);
            syncHolidays(currentYear);
        } else {
            log.info("✅ {} yılı için {} tatil mevcut, senkronizasyon atlandı", currentYear, count);
        }
    }

    @Override
    public List<GlobalSpecialDate> getUpcomingHolidays(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysAhead);

        return globalDateRepository.findByDateValueBetweenOrderByDateValueAsc(today, endDate);
    }

    @Override
    public List<GlobalSpecialDate> getHolidaysByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<GlobalSpecialDate> holidays = globalDateRepository
                .findByDateValueBetweenOrderByDateValueAsc(startDate, endDate);

        // Eğer bu yıl için tatil yoksa, senkronize et
        if (holidays.isEmpty()) {
            log.info("📅 {} yılı için tatil bulunamadı, senkronize ediliyor...", year);
            syncHolidays(year);
            holidays = globalDateRepository
                    .findByDateValueBetweenOrderByDateValueAsc(startDate, endDate);
        }

        return holidays;
    }

    @Override
    public List<GlobalSpecialDate> getAllUpcomingHolidays() {
        LocalDate today = LocalDate.now();
        return globalDateRepository.findUpcomingDates(today);
    }

    /**
     * Önemli tatil mi kontrol et
     */
    private boolean isImportantHoliday(List<String> types) {
        return types.contains("National holiday") ||
                types.contains("Observance") ||
                types.contains("Common local holiday");
    }

    /**
     * Tatil tipini belirle
     */
    private String determineHolidayType(List<String> types) {
        if (types.contains("National holiday")) {
            return "NATIONAL_HOLIDAY";
        }
        if (types.contains("Observance")) {
            return "SPECIAL_DAY";
        }
        return "NATIONAL_HOLIDAY";
    }

    /**
     * Tatil adına göre icon belirle
     */
    private String getIconForHoliday(String name) {
        String lowerName = name.toLowerCase();

        if (lowerName.contains("cumhuriyet")) return "🇹🇷";
        if (lowerName.contains("zafer")) return "🇹🇷";
        if (lowerName.contains("atatürk") || lowerName.contains("kasım")) return "🕯️";
        if (lowerName.contains("çocuk")) return "🎈";
        if (lowerName.contains("gençlik")) return "⚡";
        if (lowerName.contains("demokrasi")) return "🇹🇷";
        if (lowerName.contains("ramazan") || lowerName.contains("kurban")) return "🌙";
        if (lowerName.contains("yılbaşı") || lowerName.contains("new year")) return "🎉";
        if (lowerName.contains("sevgililer") || lowerName.contains("valentine")) return "💝";
        if (lowerName.contains("anneler") || lowerName.contains("mother")) return "👩";
        if (lowerName.contains("babalar") || lowerName.contains("father")) return "👨";
        if (lowerName.contains("kadınlar") || lowerName.contains("women")) return "👩";
        if (lowerName.contains("işçi") || lowerName.contains("mayıs") || lowerName.contains("labor")) return "⚙️";

        return "📅";
    }
}