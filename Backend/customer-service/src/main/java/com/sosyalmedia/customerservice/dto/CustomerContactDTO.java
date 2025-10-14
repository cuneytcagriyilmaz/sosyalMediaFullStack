package com.sosyalmedia.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "İletişim bilgisi")
public class CustomerContactDTO {

    @Schema(description = "İletişim ID (Response'da dolu, Request'te boş olabilir)", example = "1")
    private Long id;

    @Schema(description = "Ad", example = "Ahmet")
    @NotBlank(message = "Ad boş olamaz")
    private String name;

    @Schema(description = "Soyad", example = "Yılmaz")
    @NotBlank(message = "Soyad boş olamaz")
    private String surname;

    @Schema(description = "Email", example = "ahmet@example.com")
    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email giriniz")
    private String email;

    @Schema(description = "Telefon", example = "5551234567")
    @NotBlank(message = "Telefon boş olamaz")
    private String phone;

    @Schema(description = "Öncelik sırası (1=En yüksek)", example = "1")
    @NotNull(message = "Öncelik sırası boş olamaz")
    @Min(value = 1, message = "Öncelik en az 1 olmalıdır")
    private Integer priority;
}