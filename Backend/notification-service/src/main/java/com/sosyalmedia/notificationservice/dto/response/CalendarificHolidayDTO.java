package com.sosyalmedia.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarificHolidayDTO {
    private String name;
    private String description;
    private HolidayDate date;
    private String primaryType;  // "National holiday", "Observance"

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HolidayDate {
        private String iso;  // "2025-01-01"
    }
}