package com.sosyalmedia.customerservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "file.upload")
@Data
public class FileUploadProperties {
    private String directory;
    private List<String> allowedExtensions;
    private String baseUrl;
}