package com.sosyalmedia.notificationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateRequestDTO {

    @NotBlank(message = "Template key is required")
    private String templateKey;

    @NotBlank(message = "Template name is required")
    private String templateName;

    @NotBlank(message = "Subject template is required")
    private String subjectTemplate;

    @NotBlank(message = "Body template is required")
    private String bodyTemplate;

    private String templateType; // NOTIFICATION, REMINDER, ALERT

    private List<String> variables; // ["companyName", "contactName", "postDate"]

    private Boolean isActive;
}