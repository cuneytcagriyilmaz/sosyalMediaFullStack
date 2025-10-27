package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.request.NotificationFilterDTO;
import com.sosyalmedia.notificationservice.dto.response.NotificationDTO;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Bildirim oluştur
     * POST /api/notifications
     */
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody Notification notification) {
        log.info("🔔 Yeni bildirim oluşturuluyor");
        Notification created = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(created));
    }

    /**
     * Bildirimi okundu olarak işaretle
     * PUT /api/notifications/{id}/mark-as-read
     */
    @PutMapping("/{id}/mark-as-read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        log.info("📖 Bildirim okundu olarak işaretleniyor - ID: {}", id);
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(convertToDTO(notification));
    }

    /**
     * Toplu okundu işaretle
     * PUT /api/notifications/mark-as-read
     */
    @PutMapping("/mark-as-read")
    public ResponseEntity<Void> markMultipleAsRead(@RequestBody List<Long> notificationIds) {
        log.info("📖 {} bildirim okundu olarak işaretleniyor", notificationIds.size());
        notificationService.markMultipleAsRead(notificationIds);
        return ResponseEntity.ok().build();
    }

    /**
     * Tüm bildirimleri okundu olarak işaretle
     * PUT /api/notifications/mark-all-as-read/customer/{customerId}
     */
    @PutMapping("/mark-all-as-read/customer/{customerId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long customerId) {
        log.info("📖 Tüm bildirimler okundu olarak işaretleniyor - Customer ID: {}", customerId);
        notificationService.markAllAsRead(customerId);
        return ResponseEntity.ok().build();
    }

    /**
     * Bildirimi sil
     * DELETE /api/notifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("🗑️ Bildirim siliniyor - ID: {}", id);
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Toplu bildirim sil
     * DELETE /api/notifications
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleNotifications(@RequestBody List<Long> notificationIds) {
        log.info("🗑️ {} bildirim siliniyor", notificationIds.size());
        notificationService.deleteMultipleNotifications(notificationIds);
        return ResponseEntity.noContent().build();
    }

    /**
     * ID'ye göre bildirim getir
     * GET /api/notifications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        log.info("🔍 Bildirim getiriliyor - ID: {}", id);
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(convertToDTO(notification));
    }

    /**
     * Tüm bildirimleri getir (sayfalı)
     * GET /api/notifications
     */
    @GetMapping
    public ResponseEntity<Page<NotificationDTO>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        log.info("📋 Tüm bildirimler getiriliyor - Page: {}, Size: {}", page, size);

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Notification> notifications = notificationService.getAllNotifications(pageable);

        return ResponseEntity.ok(notifications.map(this::convertToDTO));
    }

    /**
     * Okunmamış bildirimleri getir
     * GET /api/notifications/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        log.info("📋 Okunmamış bildirimler getiriliyor");
        List<Notification> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(convertToDTOs(notifications));
    }

    /**
     * Müşteriye göre bildirimleri getir
     * GET /api/notifications/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<NotificationDTO>> getCustomerNotifications(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("📋 Müşteri bildirimleri getiriliyor - Customer ID: {}", customerId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationService.getCustomerNotifications(customerId, pageable);

        return ResponseEntity.ok(notifications.map(this::convertToDTO));
    }

    /**
     * Müşterinin okunmamış bildirimleri
     * GET /api/notifications/customer/{customerId}/unread
     */
    @GetMapping("/customer/{customerId}/unread")
    public ResponseEntity<List<NotificationDTO>> getCustomerUnreadNotifications(
            @PathVariable Long customerId
    ) {
        log.info("📋 Müşterinin okunmamış bildirimleri getiriliyor - Customer ID: {}", customerId);
        List<Notification> notifications = notificationService.getCustomerUnreadNotifications(customerId);
        return ResponseEntity.ok(convertToDTOs(notifications));
    }

    /**
     * Kritik bildirimleri getir
     * GET /api/notifications/critical
     */
    @GetMapping("/critical")
    public ResponseEntity<List<NotificationDTO>> getCriticalNotifications() {
        log.info("📋 Kritik bildirimler getiriliyor");
        List<Notification> notifications = notificationService.getCriticalNotifications();
        return ResponseEntity.ok(convertToDTOs(notifications));
    }

    /**
     * Filtreleme ile bildirimleri getir
     * POST /api/notifications/filter
     */
    @PostMapping("/filter")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsByFilter(
            @RequestBody NotificationFilterDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("🔍 Filtreli bildirimler getiriliyor");

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationService.getNotificationsByFilter(filter, pageable);

        return ResponseEntity.ok(notifications.map(this::convertToDTO));
    }

    /**
     * Okunmamış bildirim sayısı
     * GET /api/notifications/count/unread
     */
    @GetMapping("/count/unread")
    public ResponseEntity<Long> getUnreadCount() {
        log.info("📊 Okunmamış bildirim sayısı getiriliyor");
        long count = notificationService.getUnreadCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Müşterinin okunmamış bildirim sayısı
     * GET /api/notifications/count/unread/customer/{customerId}
     */
    @GetMapping("/count/unread/customer/{customerId}")
    public ResponseEntity<Long> getCustomerUnreadCount(@PathVariable Long customerId) {
        log.info("📊 Müşteri okunmamış bildirim sayısı - Customer ID: {}", customerId);
        long count = notificationService.getCustomerUnreadCount(customerId);
        return ResponseEntity.ok(count);
    }

    /**
     * Kritik okunmamış bildirim sayısı
     * GET /api/notifications/count/critical
     */
    @GetMapping("/count/critical")
    public ResponseEntity<Long> getCriticalUnreadCount() {
        log.info("📊 Kritik okunmamış bildirim sayısı getiriliyor");
        long count = notificationService.getCriticalUnreadCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Son bildirimleri getir
     * GET /api/notifications/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<NotificationDTO>> getRecentNotifications() {
        log.info("📋 Son bildirimler getiriliyor");
        List<Notification> notifications = notificationService.getRecentNotifications();
        return ResponseEntity.ok(convertToDTOs(notifications));
    }

    /**
     * Müşterinin son bildirimleri
     * GET /api/notifications/recent/customer/{customerId}
     */
    @GetMapping("/recent/customer/{customerId}")
    public ResponseEntity<List<NotificationDTO>> getCustomerRecentNotifications(
            @PathVariable Long customerId
    ) {
        log.info("📋 Müşterinin son bildirimleri - Customer ID: {}", customerId);
        List<Notification> notifications = notificationService.getCustomerRecentNotifications(customerId);
        return ResponseEntity.ok(convertToDTOs(notifications));
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private NotificationDTO convertToDTO(Notification notification) {
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

    private List<NotificationDTO> convertToDTOs(List<Notification> notifications) {
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}