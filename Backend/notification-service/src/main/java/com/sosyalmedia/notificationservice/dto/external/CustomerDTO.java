package com.sosyalmedia.notificationservice.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String companyName;
    private String sector;
    private String status; // ACTIVE, INACTIVE
    private LocalDate registrationDate;

    // İletişim bilgileri
    private ContactDTO primaryContact;

    // Hedef kitle bilgileri
    private TargetAudienceDTO targetAudience;

    // Özel gün ayarları
    private Boolean enableSpecialDayPosts;
    private List<SpecialDayPreferenceDTO> specialDayPreferences;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDTO {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private String position;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetAudienceDTO {
        private Long id;
        private String postFrequency; // "3", "5", "7"
        private String preferredPlatforms; // "Instagram,Facebook,TikTok"
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpecialDayPreferenceDTO {
        private Long id;
        private Long specialDateId;
        private String specialDateName;
        private LocalDate dateValue;
        private Boolean isEnabled;
        private String customTime; // "14:00:00"
    }
}