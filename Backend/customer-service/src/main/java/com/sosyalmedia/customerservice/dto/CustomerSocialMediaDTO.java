package com.sosyalmedia.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Sosyal medya hesap bilgileri")
public class CustomerSocialMediaDTO {

    @Schema(description = "Sosyal medya ID (Response'da dolu)", example = "1")
    private Long id;

    @Schema(description = "Instagram kullanıcı adı", example = "@premiumcoffeehouse")
    @Pattern(regexp = "^@?[a-zA-Z0-9._]{1,30}$", message = "Geçerli bir Instagram kullanıcı adı giriniz")
    private String instagram;

    @Schema(description = "Facebook sayfa adı", example = "PremiumCoffeeHouseAntalya")
    private String facebook;

    @Schema(description = "TikTok kullanıcı adı", example = "@premiumcoffee_tt")
    @Pattern(regexp = "^@?[a-zA-Z0-9._]{1,24}$", message = "Geçerli bir TikTok kullanıcı adı giriniz")
    private String tiktok;
}