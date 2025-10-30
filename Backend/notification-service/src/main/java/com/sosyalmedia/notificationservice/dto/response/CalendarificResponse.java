package com.sosyalmedia.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarificResponse {
    private CalendarificMeta meta;
    private CalendarificResponseData response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CalendarificMeta {
        private Integer code;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CalendarificResponseData {
        private List<CalendarificHolidayDTO> holidays;
    }
}