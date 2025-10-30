package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDashboardResponse {

    // Genel İstatistikler
    private DashboardStats stats;

    // Son 7 gündeki tüm firmalar ve deadline'ları (en kritikten en normale)
    private List<CompanyWithDeadlinesDTO> upcomingCompanies;

    // Kritik (0-1 gün) deadline'lar
    private List<PostDeadlineResponse> criticalDeadlines;

    // Gecikmiş deadline'lar
    private List<PostDeadlineResponse> overdueDeadlines;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStats {
        // Toplam sayılar
        private Long totalUpcoming;      // Son 7 gün içindeki toplam deadline
        private Long totalCompanies;     // Deadline'ı olan firma sayısı

        // Urgency bazlı
        private Long criticalCount;      // 0-1 gün (Kritik)
        private Long warningCount;       // 2-3 gün (Uyarı)
        private Long normalCount;        // 4-7 gün (Normal)
        private Long overdueCount;       // Gecikmiş

        // Status bazlı
        private Long notStartedCount;    // Başlanmadı
        private Long inProgressCount;    // Hazırlanıyor
        private Long readyCount;         // Hazırlandı

        // Content hazırlık durumu
        private Long contentReadyCount;     // İçerik hazır olanlar
        private Long contentNotReadyCount;  // İçerik hazır olmayanlar
    }
}