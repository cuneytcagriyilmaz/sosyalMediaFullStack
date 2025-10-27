package com.sosyalmedia.notificationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateUpdateDTO {

    @NotBlank(message = "Subject template is required")
    private String subjectTemplate;

    @NotBlank(message = "Body template is required")
    private String bodyTemplate;

    private Boolean isActive;
}