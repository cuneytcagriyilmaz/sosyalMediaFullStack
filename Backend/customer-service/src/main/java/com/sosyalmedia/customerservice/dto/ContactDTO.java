package com.sosyalmedia.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "İletişim bilgisi")
public class ContactDTO {

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
    private Integer priority;
}