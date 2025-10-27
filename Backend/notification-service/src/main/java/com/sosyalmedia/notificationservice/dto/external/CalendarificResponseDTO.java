package com.sosyalmedia.notificationservice.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CalendarificResponseDTO {
    private Meta meta;
    private Response response;

    @Data
    public static class Meta {
        private int code;
    }

    @Data
    public static class Response {
        private List<Holiday> holidays;
    }

    @Data
    public static class Holiday {
        private String name;
        private String description;
        private HolidayDate date;
        private List<String> type;

        @Data
        public static class HolidayDate {
            private String iso;

            @JsonProperty("datetime")
            private DateTimeInfo dateTime;

            @Data
            public static class DateTimeInfo {
                private int year;
                private int month;
                private int day;
            }
        }
    }
}