package com.sosyalmedia.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "SEO bilgileri")
public class CustomerSeoDTO {

    @Schema(description = "SEO ID (Response'da dolu)", example = "1")
    private Long id;

    @Schema(description = "Google Console email adresi", example = "seo@premiumcoffee.com")
    @Email(message = "Geçerli bir email giriniz")
    private String googleConsoleEmail;

    @Schema(description = "SEO başlık önerileri", example = "Antalya'nın En İyi Kahve Deneyimi | Premium Coffee House")
    private String titleSuggestions;

    @Schema(description = "SEO içerik önerileri", example = "Özel çekim kahveler, barista şampiyonları, sanatsal latte art")
    private String contentSuggestions;
}