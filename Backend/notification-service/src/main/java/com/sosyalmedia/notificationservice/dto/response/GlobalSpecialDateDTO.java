package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSpecialDateDTO {
    private Long id;
    private String dateType;
    private String dateName;
    private LocalDate dateValue;
    private String description;
    private String icon;
    private Boolean isRecurring;
    private Boolean autoSuggestPost;
}