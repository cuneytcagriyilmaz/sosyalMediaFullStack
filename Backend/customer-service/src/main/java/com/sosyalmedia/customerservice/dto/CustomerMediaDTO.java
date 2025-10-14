package com.sosyalmedia.customerservice.dto;

import com.sosyalmedia.customerservice.entity.CustomerMedia;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Müşteri medya dosyası bilgileri")
public class CustomerMediaDTO {

    @Schema(description = "Medya ID", example = "1")
    private Long id;

    @Schema(description = "Dosya yolu", example = "premium-coffee-house/logos/premium-coffee-house-logo.png")
    private String filePath;

    @Schema(description = "Medya türü", example = "LOGO")
    private CustomerMedia.MediaType mediaType;

    @Schema(description = "Orijinal dosya adı", example = "logo.png")
    private String originalFileName;

    @Schema(description = "Dosya boyutu (byte)", example = "524288")
    private Long fileSize;

    @Schema(description = "Tam URL (Frontend için)", example = "http://localhost:8080/uploads/premium-coffee-house/logos/logo.png")
    private String fullUrl;
}