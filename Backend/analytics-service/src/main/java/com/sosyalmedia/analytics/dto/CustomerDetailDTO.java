// src/main/java/com/sosyalmedia/analytics/dto/CustomerDetailDTO.java

package com.sosyalmedia.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailDTO {

    // Genel bilgiler
    private Long id;
    private String companyName;
    private String sector;
    private String email;
    private String phone;
    private String contactPerson;
    private String status;
    private String membershipPackage;
    private LocalDateTime createdAt;

    // Sosyal medya bağlantıları
    private Map<String, Boolean> socialMediaConnected;

    // İçerik bilgileri
    private List<String> hashtags;
    private Integer photosUploaded;

    // Post istatistikleri
    private PostStatsDTO postStats;

    // Notlar
    private List<CustomerNoteDTO> notes;
}