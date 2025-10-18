package com.sosyalmedia.customerservice.dto.response;

import com.sosyalmedia.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Müşteri liste görünümü (özet bilgi)")
public class CustomerListResponse {

    @Schema(description = "Müşteri ID", example = "1")
    private Long id;

    @Schema(description = "Şirket adı", example = "Cafe Sunshine")
    private String companyName;

    @Schema(description = "Sektör", example = "cafe")
    private String sector;

    @Schema(description = "Üyelik paketi", example = "Gold")
    private String membershipPackage;

    @Schema(description = "Durum", example = "ACTIVE")
    private Customer.CustomerStatus status;

    @Schema(description = "Oluşturulma tarihi")
    private LocalDateTime createdAt;
}