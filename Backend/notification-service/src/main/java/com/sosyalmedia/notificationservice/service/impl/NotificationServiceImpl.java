package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.request.NotificationFilterDTO;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.repository.NotificationRepository;
import com.sosyalmedia.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public Notification createNotification(Notification notification) {
        log.info("🔔 Yeni bildirim oluşturuluyor: {}", notification.getTitle());

        Notification saved = notificationRepository.save(notification);

        log.debug("✅ Bildirim kaydedildi - ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public List<Notification> createNotificationsBulk(List<Notification> notifications) {
        log.info("🔔 {} adet bildirim toplu olarak oluşturuluyor", notifications.size());

        List<Notification> saved = notificationRepository.saveAll(notifications);

        log.info("✅ {} bildirim başarıyla kaydedildi", saved.size());
        return saved;
    }

    @Override
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
            log.debug("✅ Bildirim okundu olarak işaretlendi - ID: {}", notificationId);
        }

        return notification;
    }

    @Override
    @Transactional
    public void markMultipleAsRead(List<Long> notificationIds) {
        log.info("📖 {} bildirim okundu olarak işaretleniyor", notificationIds.size());

        for (Long id : notificationIds) {
            try {
                markAsRead(id);
            } catch (ResourceNotFoundException e) {
                log.warn("⚠️ Bildirim bulunamadı - ID: {}", id);
            }
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(Long customerId) {
        List<Notification> unreadNotifications = notificationRepository
                .findByCustomerIdAndIsReadFalse(customerId);

        log.info("📖 {} numaralı müşterinin {} okunmamış bildirimi işaretleniyor",
                customerId, unreadNotifications.size());

        LocalDateTime now = LocalDateTime.now();

        unreadNotifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);

        log.info("✅ Tüm bildirimler okundu olarak işaretlendi");
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification", "id", notificationId);
        }

        notificationRepository.deleteById(notificationId);
        log.info("🗑️ Bildirim silindi - ID: {}", notificationId);
    }

    @Override
    @Transactional
    public void deleteMultipleNotifications(List<Long> notificationIds) {
        log.info("🗑️ {} bildirim siliniyor", notificationIds.size());

        for (Long id : notificationIds) {
            try {
                deleteNotification(id);
            } catch (ResourceNotFoundException e) {
                log.warn("⚠️ Bildirim bulunamadı - ID: {}", id);
            }
        }
    }

    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
    }

    @Override
    public Page<Notification> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    @Override
    public Page<Notification> getUnreadNotifications(Pageable pageable) {
        return notificationRepository.findByIsReadFalse(pageable);
    }

    @Override
    public List<Notification> getCustomerNotifications(Long customerId) {
        return notificationRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public Page<Notification> getCustomerNotifications(Long customerId, Pageable pageable) {
        return notificationRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    public List<Notification> getCustomerUnreadNotifications(Long customerId) {
        List<Notification> allNotifications = notificationRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId);

        return allNotifications.stream()
                .filter(n -> !n.getIsRead())
                .toList();
    }

    @Override
    public List<Notification> getCriticalNotifications() {
        return notificationRepository.findBySeverityOrderByCreatedAtDesc("CRITICAL");
    }

    @Override
    public Page<Notification> getNotificationsByFilter(NotificationFilterDTO filter, Pageable pageable) {
        LocalDateTime startDate = filter.getStartDate() != null
                ? filter.getStartDate().atStartOfDay()
                : null;

        LocalDateTime endDate = filter.getEndDate() != null
                ? filter.getEndDate().atTime(23, 59, 59)
                : null;

        return notificationRepository.findByFilters(
                filter.getCustomerId(),
                filter.getNotificationType(),
                filter.getSeverity(),
                filter.getIsRead(),
                startDate,
                endDate,
                pageable
        );
    }

    @Override
    public long getUnreadCount() {
        return notificationRepository.countByIsReadFalse();
    }

    @Override
    public long getCustomerUnreadCount(Long customerId) {
        return notificationRepository.countByCustomerIdAndIsReadFalse(customerId);
    }

    @Override
    public long getCriticalUnreadCount() {
        return notificationRepository.countByIsReadFalseAndSeverity("CRITICAL");
    }

    @Override
    public List<Notification> getRecentNotifications() {
        return notificationRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Notification> getCustomerRecentNotifications(Long customerId) {
        return notificationRepository.findTop5ByCustomerIdOrderByCreatedAtDesc(customerId);
    }
}