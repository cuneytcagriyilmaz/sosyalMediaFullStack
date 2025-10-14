package com.sosyalmedia.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "API anahtarları")
public class CustomerApiKeyDTO {

    @Schema(description = "API Key ID (Response'da dolu)", example = "1")
    private Long id;

    @Schema(description = "Instagram API anahtarı", example = "INST_API_KEY_12345")
    private String instagramApiKey;

    @Schema(description = "Facebook API anahtarı", example = "FB_API_KEY_67890")
    private String facebookApiKey;

    @Schema(description = "TikTok API anahtarı", example = "TT_API_KEY_ABCDE")
    private String tiktokApiKey;

    @Schema(description = "Google API anahtarı", example = "GOOGLE_API_KEY_XYZ")
    private String googleApiKey;
}