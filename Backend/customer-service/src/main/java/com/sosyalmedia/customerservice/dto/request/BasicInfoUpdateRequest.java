// BasicInfoUpdateRequest.java - Yeni dosya oluştur

package com.sosyalmedia.customerservice.dto.request;


import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Temel bilgi güncelleme talebi")
public class BasicInfoUpdateRequest {

    @NotBlank(message = "Şirket adı zorunludur")
    @Schema(description = "Şirket adı", example = "Kahve Dünyası")
    private String companyName;

    @NotBlank(message = "Sektör zorunludur")
    @Schema(description = "Sektör", example = "Cafe & Restaurant")
    private String sector;

    @NotBlank(message = "Adres zorunludur")
    @Schema(description = "Tam adres", example = "Lara, Antalya")
    private String address;

    @NotBlank(message = "Üyelik paketi zorunludur")
    @Schema(description = "Üyelik paketi", example = "Gold")
    private String membershipPackage;

    @NotNull(message = "Durum zorunludur")
    @Schema(description = "Müşteri durumu", example = "ACTIVE")
    private Customer.CustomerStatus status;
}