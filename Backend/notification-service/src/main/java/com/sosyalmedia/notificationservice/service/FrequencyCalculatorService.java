package com.sosyalmedia.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FrequencyCalculatorService {

    /**
     * Verilen başlangıç tarihinden itibaren N adet post tarihi hesaplar
     *
     * @param startDate Başlangıç tarihi
     * @param frequency Haftalık post sayısı (1-7)
     * @param count Kaç tane post oluşturulacak
     * @return Post tarihleri listesi
     */
    public List<LocalDate> calculatePostDates(LocalDate startDate, Integer frequency, int count) {
        log.info("📅 Calculating {} post dates starting from {} with frequency: {} posts/week",
                count, startDate, frequency);

        if (frequency == null || frequency < 1 || frequency > 7) {
            log.warn("⚠️ Invalid frequency: {}, defaulting to 3", frequency);
            frequency = 3;
        }

        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        for (int i = 0; i < count; i++) {
            dates.add(currentDate);
            currentDate = calculateNextDate(currentDate, frequency);
        }

        log.info("✅ Calculated {} dates: first={}, last={}",
                dates.size(), dates.get(0), dates.get(dates.size() - 1));

        return dates;
    }

    /**
     * Haftalık post sayısına göre bir sonraki post tarihini hesaplar
     *
     * @param current Şu anki tarih
     * @param postsPerWeek Haftalık post sayısı (1-7)
     * @return Bir sonraki post tarihi
     */
    private LocalDate calculateNextDate(LocalDate current, int postsPerWeek) {
        return switch (postsPerWeek) {
            case 1 -> getNextWeekday(current, DayOfWeek.MONDAY);  // Her Pazartesi
            case 2 -> getNextFromDays(current, DayOfWeek.MONDAY, DayOfWeek.THURSDAY);  // Pzt, Prş
            case 3 -> getNextFromDays(current, DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);  // Pzt, Çar, Cum
            case 4 -> getNextFromDays(current, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);  // Pzt, Sal, Prş, Cum
            case 5 -> getNextWeekday(current);  // Pazartesi-Cuma (her iş günü)
            case 6 -> getNextFromDays(current, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);  // Pzt-Cmt
            case 7 -> current.plusDays(1);  // Her gün
            default -> {
                log.warn("⚠️ Invalid posts per week: {}, defaulting to 3", postsPerWeek);
                yield getNextFromDays(current, DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
            }
        };
    }

    /**
     * Belirli bir gün için bir sonraki o günü bulur
     * Örnek: Current = Pazartesi → Next = Bir sonraki Pazartesi
     */
    private LocalDate getNextWeekday(LocalDate current, DayOfWeek targetDay) {
        LocalDate next = current.plusDays(1);
        while (next.getDayOfWeek() != targetDay) {
            next = next.plusDays(1);
        }
        return next;
    }

    /**
     * Birden fazla gün arasında sıradaki günü bulur
     * Örnek: [Pazartesi, Çarşamba, Cuma] → Current Pazartesi ise → Çarşamba
     */
    private LocalDate getNextFromDays(LocalDate current, DayOfWeek... targetDays) {
        LocalDate next = current.plusDays(1);
        int maxAttempts = 7;
        int attempts = 0;

        while (attempts < maxAttempts) {
            for (DayOfWeek targetDay : targetDays) {
                if (next.getDayOfWeek() == targetDay) {
                    return next;
                }
            }
            next = next.plusDays(1);
            attempts++;
        }

        // Fallback: bir sonraki haftanın ilk gününü döndür
        return getNextWeekday(current, targetDays[0]);
    }

    /**
     * Bir sonraki hafta içi günü (Pazartesi-Cuma)
     */
    private LocalDate getNextWeekday(LocalDate current) {
        LocalDate next = current.plusDays(1);

        while (next.getDayOfWeek() == DayOfWeek.SATURDAY ||
                next.getDayOfWeek() == DayOfWeek.SUNDAY) {
            next = next.plusDays(1);
        }

        return next;
    }
}