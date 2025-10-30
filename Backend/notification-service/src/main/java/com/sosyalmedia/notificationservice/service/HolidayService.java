package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.client.CalendarificClient;
import com.sosyalmedia.notificationservice.dto.response.CalendarificHolidayDTO;
import com.sosyalmedia.notificationservice.dto.response.CalendarificResponse;
import com.sosyalmedia.notificationservice.entity.TurkishHoliday;
import com.sosyalmedia.notificationservice.repository.TurkishHolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayService {

    private final CalendarificClient calendarificClient;
    private final TurkishHolidayRepository holidayRepository;

    @Value("${calendarific.api-key:QvzVcvgwJ49qCz7zBe7DYA1jZb63cxws}")
    private String apiKey;

    @Value("${calendarific.country:TR}")
    private String country;

    /**
     * Verilen tarih aralığındaki tüm resmi tatilleri getirir
     */
    @Transactional
    public List<TurkishHoliday> getHolidaysInRange(LocalDate startDate, LocalDate endDate) {
        log.info("🎉 Getting holidays between {} and {}", startDate, endDate);

        Set<Integer> years = new HashSet<>();

        // Aralıktaki tüm yılları bul
        for (int year = startDate.getYear(); year <= endDate.getYear(); year++) {
            years.add(year);
        }

        // Her yıl için tatilleri çek
        List<TurkishHoliday> allHolidays = new ArrayList<>();
        for (Integer year : years) {
            fetchAndCacheHolidays(year);  // Cache'e yükle

            List<TurkishHoliday> yearHolidays = holidayRepository
                    .findByYearAndDateBetween(year, startDate, endDate);
            allHolidays.addAll(yearHolidays);
        }

        log.info("✅ Found {} holidays in range", allHolidays.size());
        return allHolidays;
    }

    /**
     * Verilen yıl için tatilleri API'den çeker ve cache'ler
     * Cache'de varsa tekrar çekmez
     */
    @Transactional
    public void fetchAndCacheHolidays(int year) {
        // Cache kontrolü
        if (holidayRepository.existsByYear(year)) {
            log.debug("📦 Holidays for year {} already cached", year);
            return;
        }

        log.info("🌐 Fetching holidays from Calendarific API for year {}", year);

        try {
            CalendarificResponse response = calendarificClient.getHolidays(apiKey, country, year);

            if (response == null || response.getResponse() == null) {
                log.warn("⚠️ Empty response from Calendarific API for year {}", year);
                return;
            }

            List<CalendarificHolidayDTO> holidays = response.getResponse().getHolidays();
            log.info("📥 Fetched {} holidays from API for year {}", holidays.size(), year);

            // Sadece "National holiday" türündeki tatilleri filtrele ve kaydet
            List<TurkishHoliday> nationalHolidays = holidays.stream()
                    .filter(h -> "National holiday".equals(h.getPrimaryType()))
                    .map(h -> TurkishHoliday.builder()
                            .year(year)
                            .date(LocalDate.parse(h.getDate().getIso()))
                            .name(h.getName())
                            .description(h.getDescription())
                            .primaryType(h.getPrimaryType())
                            .build())
                    .toList();

            if (!nationalHolidays.isEmpty()) {
                holidayRepository.saveAll(nationalHolidays);
                log.info("✅ Cached {} national holidays for year {}", nationalHolidays.size(), year);
            } else {
                log.warn("⚠️ No national holidays found for year {}", year);
            }

        } catch (Exception e) {
            log.error("❌ Failed to fetch holidays from Calendarific for year {}: {}",
                    year, e.getMessage(), e);
        }
    }

    /**
     * ✅ YENİ: Cache'lenmiş tüm tatilleri getir
     */
    @Transactional(readOnly = true)
    public List<TurkishHoliday> getAllCachedHolidays() {
        return holidayRepository.findAll();
    }
}