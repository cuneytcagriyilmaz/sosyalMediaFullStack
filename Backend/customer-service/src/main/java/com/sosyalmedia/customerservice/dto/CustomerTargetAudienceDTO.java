package com.sosyalmedia.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Hedef kitle ve içerik tercihleri")
public class CustomerTargetAudienceDTO {

    @Schema(description = "Hedef kitle ID (Response'da dolu)", example = "1")
    private Long id;

    @Schema(description = "Özel günler için post yapılsın mı", example = "true", defaultValue = "false")
    @Builder.Default
    private Boolean specialDates = false;

    @Schema(description = "Hedef bölge", example = "Antalya Merkez")
    private String targetRegion;

    @Schema(description = "Müşterinin önerdiği hashtagler", example = "#kahve #antalya #premiumcoffee #latte #espresso")
    private String customerHashtags;

    @Schema(description = "Post türü", example = "gorsel", allowableValues = {"gorsel", "video", "story", "genel"})
    @NotBlank(message = "Post türü boş olamaz")
    private String postType;

    @Schema(description = "Haftalık post sıklığı (gün sayısı)", example = "7")
    @NotBlank(message = "Post sıklığı boş olamaz")
    @Pattern(regexp = "^[1-7]$", message = "Post sıklığı 1-7 arasında olmalıdır")
    private String postFrequency;

    @Schema(description = "Post tonu", example = "samimi", allowableValues = {"samimi", "resmi", "mizahi", "ciddi"})
    @NotBlank(message = "Post tonu boş olamaz")
    private String postTone;

    @Schema(description = "Hedef kitle yaş aralığı", example = "25-45")
    private String audienceAge;

    @Schema(description = "Hedef kitle ilgi alanları", example = "Kahve, Sanat, Müzik, Kitap")
    private String audienceInterests;
}