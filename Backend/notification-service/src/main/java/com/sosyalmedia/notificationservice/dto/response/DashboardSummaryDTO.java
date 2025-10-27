package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {

    // Sayısal İstatistikler
    private int todayPostsCount;
    private int weekPostsCount;
    private int monthPostsCount; // ✅ Eklendi
    private int totalScheduledPostsCount; // ✅ Eklendi

    private int unreadNotificationsCount;
    private int totalNotificationsCount; // ✅ Eklendi
    private int criticalAlertsCount;

    private int upcomingSpecialDatesCount;
    private int thisMonthSpecialDatesCount; // ✅ Eklendi

    private int overduePostsCount; // ✅ Eklendi
    private int readyPostsCount; // ✅ Eklendi
    private int publishedPostsCount; // ✅ Eklendi

    // Oranlar
    private double emailSuccessRate;
    private double postCompletionRate; // ✅ Eklendi (hazır olan / toplam)

    // Listeler
    private List<MockScheduledPostDTO> todayPosts;
    private List<MockScheduledPostDTO> weekPosts; // ✅ Eklendi
    private List<MockScheduledPostDTO> criticalPosts;
    private List<MockScheduledPostDTO> overduePosts; // ✅ Eklendi

    private List<GlobalSpecialDateDTO> upcomingSpecialDates;
    private List<GlobalSpecialDateDTO> thisMonthSpecialDates; // ✅ Eklendi

    private List<NotificationDTO> recentNotifications;
    private List<NotificationDTO> unreadNotifications; // ✅ Eklendi

    // Event bilgileri
    private int todayEventsCount; // ✅ Eklendi
    private int weekEventsCount; // ✅ Eklendi

    // Zaman damgaları
    private LocalDateTime generatedAt; // ✅ Eklendi
    private Long customerId; // ✅ Eklendi (müşteriye özel dashboard ise)
}