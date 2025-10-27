package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateDTO {
    private Long id;
    private String templateKey;
    private String templateName;
    private String subjectTemplate;
    private String bodyTemplate;
    private String templateType;
    private List<String> variables;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}