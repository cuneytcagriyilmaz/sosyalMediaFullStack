package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.dto.response.CalendarificResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "calendarific-api",
        url = "https://calendarific.com/api/v2"
)
public interface CalendarificClient {

    @GetMapping("/holidays")
    CalendarificResponse getHolidays(
            @RequestParam("api_key") String apiKey,
            @RequestParam("country") String country,
            @RequestParam("year") Integer year
    );
}