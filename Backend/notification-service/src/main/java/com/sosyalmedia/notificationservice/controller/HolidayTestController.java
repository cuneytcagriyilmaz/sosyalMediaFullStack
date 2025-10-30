package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.entity.TurkishHoliday;
import com.sosyalmedia.notificationservice.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications/holidays")
@RequiredArgsConstructor
public class HolidayTestController {

    private final HolidayService holidayService;

    /**
     * Belirli yıl için tatilleri çek ve cache'le
     */
    @PostMapping("/fetch/{year}")
    public ResponseEntity<ApiResponse<String>> fetchHolidays(@PathVariable Integer year) {
        holidayService.fetchAndCacheHolidays(year);
        return ResponseEntity.ok(ApiResponse.success(year + " yılı tatilleri cache'lendi"));
    }

    /**
     * Tarih aralığındaki tatilleri getir
     */
    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<TurkishHoliday>>> getHolidaysInRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<TurkishHoliday> holidays = holidayService.getHolidaysInRange(start, end);

        return ResponseEntity.ok(ApiResponse.success(holidays));
    }

    /**
     * Cache'lenmiş tüm tatilleri getir
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TurkishHoliday>>> getAllCachedHolidays() {
        // Repository'den direkt çek
        return ResponseEntity.ok(ApiResponse.success(
                holidayService.getAllCachedHolidays()
        ));
    }
}