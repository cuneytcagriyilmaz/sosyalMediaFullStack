package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.response.DashboardSummaryDTO;
import com.sosyalmedia.notificationservice.dto.response.GlobalSpecialDateDTO;
import com.sosyalmedia.notificationservice.dto.response.MockScheduledPostDTO;
import com.sosyalmedia.notificationservice.dto.response.NotificationDTO;
import com.sosyalmedia.notificationservice.model.CalendarEvent;
import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;
import com.sosyalmedia.notificationservice.model.MockScheduledPost;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final MockPostService mockPostService;
    private final NotificationService notificationService;
    private final CalendarificService calendarificService;
    private final CalendarEventService calendarEventService;

    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        log.info("📊 Dashboard özeti hazırlanıyor...");

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        // Bugünün postları
        List<MockScheduledPost> todayPosts = mockPostService.getTodayPosts();

        // Bu haftanın postları (7 gün)
        List<MockScheduledPost> weekPosts = mockPostService.getUpcomingPosts(7);

        // Bu ayın postları
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59);
        List<MockScheduledPost> monthPosts = mockPostService.getPostsByDateRange(monthStart, monthEnd);

        // Gecikmiş postlar
        List<MockScheduledPost> overduePosts = mockPostService.getOverduePosts();

        // Kritik postlar (3 gün içinde ve hazır değil)
        List<MockScheduledPost> criticalPosts = mockPostService.getCriticalPosts(3);

        // Post durum istatistikleri
        long readyPostsCount = weekPosts.stream()
                .filter(p -> "READY".equals(p.getPreparationStatus()))
                .count();

        long publishedPostsCount = monthPosts.stream()
                .filter(p -> "PUBLISHED".equals(p.getStatus()))
                .count();

        long totalScheduledPosts = monthPosts.stream()
                .filter(p -> !"PUBLISHED".equals(p.getStatus()))
                .count();

        // Post tamamlanma oranı
        double postCompletionRate = totalScheduledPosts > 0
                ? (readyPostsCount * 100.0) / totalScheduledPosts
                : 100.0;

        // Bildirimler
        List<Notification> allNotifications = notificationService.getRecentNotifications();
        List<Notification> unreadNotifications = notificationService.getUnreadNotifications();
        long unreadCount = notificationService.getUnreadCount();
        long criticalAlertsCount = notificationService.getCriticalUnreadCount();

        // E-posta başarı oranı
        double emailSuccessRate = calculateEmailSuccessRate(allNotifications);

        // Yaklaşan özel günler (30 gün)
        List<GlobalSpecialDate> upcomingSpecialDates = calendarificService.getUpcomingHolidays(30);

        // Bu ayki özel günler
        List<GlobalSpecialDate> thisMonthSpecialDates = upcomingSpecialDates.stream()
                .filter(sd -> sd.getDateValue().getYear() == today.getYear()
                        && sd.getDateValue().getMonthValue() == today.getMonthValue())
                .collect(Collectors.toList());

        // Event istatistikleri
        List<CalendarEvent> todayEvents = calendarEventService.getTodayEvents();
        List<CalendarEvent> weekEvents = calendarEventService.getEventsByDateRange(
                now,
                now.plusDays(7)
        );

        DashboardSummaryDTO summary = DashboardSummaryDTO.builder()
                // Sayısal İstatistikler
                .todayPostsCount(todayPosts.size())
                .weekPostsCount(weekPosts.size())
                .monthPostsCount(monthPosts.size())
                .totalScheduledPostsCount((int) totalScheduledPosts)
                .unreadNotificationsCount((int) unreadCount)
                .totalNotificationsCount(allNotifications.size())
                .criticalAlertsCount((int) criticalAlertsCount)
                .upcomingSpecialDatesCount(upcomingSpecialDates.size())
                .thisMonthSpecialDatesCount(thisMonthSpecialDates.size())
                .overduePostsCount(overduePosts.size())
                .readyPostsCount((int) readyPostsCount)
                .publishedPostsCount((int) publishedPostsCount)
                // Oranlar
                .emailSuccessRate(emailSuccessRate)
                .postCompletionRate(postCompletionRate)
                // Listeler
                .todayPosts(convertToPostDTOs(todayPosts))
                .weekPosts(convertToPostDTOs(weekPosts))
                .criticalPosts(convertToPostDTOs(criticalPosts))
                .overduePosts(convertToPostDTOs(overduePosts))
                .upcomingSpecialDates(convertToSpecialDateDTOs(upcomingSpecialDates))
                .thisMonthSpecialDates(convertToSpecialDateDTOs(thisMonthSpecialDates))
                .recentNotifications(convertToNotificationDTOs(allNotifications))
                .unreadNotifications(convertToNotificationDTOs(unreadNotifications))
                // Event bilgileri
                .todayEventsCount(todayEvents.size())
                .weekEventsCount(weekEvents.size())
                // Zaman damgası
                .generatedAt(LocalDateTime.now())
                .customerId(null) // Genel dashboard
                .build();

        log.info("✅ Dashboard özeti hazırlandı");
        return summary;
    }

    @Override
    public DashboardSummaryDTO getCustomerDashboardSummary(Long customerId) {
        log.info("📊 Müşteri dashboard özeti hazırlanıyor - Customer ID: {}", customerId);

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        // Bugünün postları (müşteriye özel)
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(23, 59);
        List<MockScheduledPost> todayPosts = mockPostService.getCustomerPostsByDateRange(
                customerId, todayStart, todayEnd
        );

        // Bu haftanın postları
        List<MockScheduledPost> weekPosts = mockPostService.getCustomerUpcomingPosts(customerId, 7);

        // Bu ayın postları
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59);
        List<MockScheduledPost> monthPosts = mockPostService.getCustomerPostsByDateRange(
                customerId, monthStart, monthEnd
        );

        // Gecikmiş postlar (müşteriye özel)
        List<MockScheduledPost> allCustomerPosts = mockPostService.getCustomerPostsList(customerId);
        List<MockScheduledPost> overduePosts = allCustomerPosts.stream()
                .filter(post -> post.getScheduledDate().isBefore(now)
                        && !"PUBLISHED".equals(post.getStatus()))
                .collect(Collectors.toList());

        // Kritik postlar (3 gün içinde ve hazır değil)
        LocalDateTime threeDaysLater = now.plusDays(3);
        List<MockScheduledPost> criticalPosts = weekPosts.stream()
                .filter(post -> post.getScheduledDate().isBefore(threeDaysLater)
                        && !"READY".equals(post.getPreparationStatus())
                        && !"PUBLISHED".equals(post.getStatus()))
                .collect(Collectors.toList());

        // Post durum istatistikleri
        long readyPostsCount = weekPosts.stream()
                .filter(p -> "READY".equals(p.getPreparationStatus()))
                .count();

        long publishedPostsCount = monthPosts.stream()
                .filter(p -> "PUBLISHED".equals(p.getStatus()))
                .count();

        long totalScheduledPosts = monthPosts.stream()
                .filter(p -> !"PUBLISHED".equals(p.getStatus()))
                .count();

        // Post tamamlanma oranı
        double postCompletionRate = totalScheduledPosts > 0
                ? (readyPostsCount * 100.0) / totalScheduledPosts
                : 100.0;

        // Bildirimler (müşteriye özel)
        List<Notification> allNotifications = notificationService.getCustomerNotifications(customerId);
        List<Notification> unreadNotifications = notificationService.getCustomerUnreadNotifications(customerId);
        List<Notification> recentNotifications = notificationService.getCustomerRecentNotifications(customerId);
        long unreadCount = notificationService.getCustomerUnreadCount(customerId);

        // Kritik bildirimler (müşteriye özel)
        long criticalAlertsCount = unreadNotifications.stream()
                .filter(n -> "CRITICAL".equals(n.getSeverity()))
                .count();

        // E-posta başarı oranı
        double emailSuccessRate = calculateEmailSuccessRate(allNotifications);

        // Yaklaşan özel günler (30 gün) - tüm müşteriler için aynı
        List<GlobalSpecialDate> upcomingSpecialDates = calendarificService.getUpcomingHolidays(30);

        // Bu ayki özel günler
        List<GlobalSpecialDate> thisMonthSpecialDates = upcomingSpecialDates.stream()
                .filter(sd -> sd.getDateValue().getYear() == today.getYear()
                        && sd.getDateValue().getMonthValue() == today.getMonthValue())
                .collect(Collectors.toList());

        // Event istatistikleri (müşteriye özel)
        List<CalendarEvent> todayEvents = calendarEventService.getCustomerEventsByDateRange(
                customerId, todayStart, todayEnd
        );
        List<CalendarEvent> weekEvents = calendarEventService.getCustomerEventsByDateRange(
                customerId, now, now.plusDays(7)
        );

        DashboardSummaryDTO summary = DashboardSummaryDTO.builder()
                // Sayısal İstatistikler
                .todayPostsCount(todayPosts.size())
                .weekPostsCount(weekPosts.size())
                .monthPostsCount(monthPosts.size())
                .totalScheduledPostsCount((int) totalScheduledPosts)
                .unreadNotificationsCount((int) unreadCount)
                .totalNotificationsCount(allNotifications.size())
                .criticalAlertsCount((int) criticalAlertsCount)
                .upcomingSpecialDatesCount(upcomingSpecialDates.size())
                .thisMonthSpecialDatesCount(thisMonthSpecialDates.size())
                .overduePostsCount(overduePosts.size())
                .readyPostsCount((int) readyPostsCount)
                .publishedPostsCount((int) publishedPostsCount)
                // Oranlar
                .emailSuccessRate(emailSuccessRate)
                .postCompletionRate(postCompletionRate)
                // Listeler
                .todayPosts(convertToPostDTOs(todayPosts))
                .weekPosts(convertToPostDTOs(weekPosts))
                .criticalPosts(convertToPostDTOs(criticalPosts))
                .overduePosts(convertToPostDTOs(overduePosts))
                .upcomingSpecialDates(convertToSpecialDateDTOs(upcomingSpecialDates))
                .thisMonthSpecialDates(convertToSpecialDateDTOs(thisMonthSpecialDates))
                .recentNotifications(convertToNotificationDTOs(recentNotifications))
                .unreadNotifications(convertToNotificationDTOs(unreadNotifications))
                // Event bilgileri
                .todayEventsCount(todayEvents.size())
                .weekEventsCount(weekEvents.size())
                // Zaman damgası
                .generatedAt(LocalDateTime.now())
                .customerId(customerId) // Müşteriye özel dashboard
                .build();

        log.info("✅ Müşteri dashboard özeti hazırlandı - Customer ID: {}", customerId);
        return summary;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * E-posta başarı oranını hesapla
     */
    private double calculateEmailSuccessRate(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            return 100.0;
        }

        long totalEmails = notifications.stream()
                .filter(n -> n.getEmailSent() != null && n.getEmailSent())
                .count();

        if (totalEmails == 0) {
            return 100.0;
        }

        long successfulEmails = notifications.stream()
                .filter(n -> n.getEmailSent() != null && n.getEmailSent()
                        && "SENT".equals(n.getEmailStatus()))
                .count();

        return Math.round((successfulEmails * 100.0) / totalEmails * 100.0) / 100.0;
    }

    /**
     * MockScheduledPost -> MockScheduledPostDTO
     */
    private List<MockScheduledPostDTO> convertToPostDTOs(List<MockScheduledPost> posts) {
        return posts.stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());
    }

    private MockScheduledPostDTO convertToPostDTO(MockScheduledPost post) {
        return MockScheduledPostDTO.builder()
                .id(post.getId())
                .customerId(post.getCustomerId())
                .scheduledDate(post.getScheduledDate())
                .postType(post.getPostType())
                .isSpecialDayPost(post.getIsSpecialDayPost())
                .specialDateId(post.getSpecialDateId())
                .status(post.getStatus())
                .preparationStatus(post.getPreparationStatus())
                .content(post.getContent())
                .platforms(post.getPlatforms())
                .build();
    }

    /**
     * GlobalSpecialDate -> GlobalSpecialDateDTO
     */
    private List<GlobalSpecialDateDTO> convertToSpecialDateDTOs(List<GlobalSpecialDate> specialDates) {
        return specialDates.stream()
                .map(this::convertToSpecialDateDTO)
                .collect(Collectors.toList());
    }

    private GlobalSpecialDateDTO convertToSpecialDateDTO(GlobalSpecialDate specialDate) {
        return GlobalSpecialDateDTO.builder()
                .id(specialDate.getId())
                .dateType(specialDate.getDateType())
                .dateName(specialDate.getDateName())
                .dateValue(specialDate.getDateValue())
                .description(specialDate.getDescription())
                .icon(specialDate.getIcon())
                .isRecurring(specialDate.getIsRecurring())
                .autoSuggestPost(specialDate.getAutoSuggestPost())
                .build();
    }

    /**
     * Notification -> NotificationDTO
     */
    private List<NotificationDTO> convertToNotificationDTOs(List<Notification> notifications) {
        return notifications.stream()
                .map(this::convertToNotificationDTO)
                .collect(Collectors.toList());
    }

    private NotificationDTO convertToNotificationDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .customerId(notification.getCustomerId())
                .postId(notification.getPostId())
                .specialDateId(notification.getSpecialDateId())
                .notificationType(notification.getNotificationType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .icon(notification.getIcon())
                .severity(notification.getSeverity())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .emailSent(notification.getEmailSent())
                .emailSentAt(notification.getEmailSentAt())
                .emailStatus(notification.getEmailStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}