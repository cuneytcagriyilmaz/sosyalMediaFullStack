package com.sosyalmedia.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineSummaryDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String eventType;           // FIRST_POST, REGULAR, SPECIAL_DATE
    private String eventTypeDisplayName;
    private String platform;
    private String platformDisplayName;
    private String holidayName;         // Sadece SPECIAL_DATE için
}