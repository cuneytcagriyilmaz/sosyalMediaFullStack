package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.NotificationFilterDTO;
import com.sosyalmedia.notificationservice.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    /**
     * Bildirim oluştur
     */
    Notification createNotification(Notification notification);

    /**
     * Toplu bildirim oluştur
     */
    List<Notification> createNotificationsBulk(List<Notification> notifications);

    /**
     * Bildirimi okundu olarak işaretle
     */
    Notification markAsRead(Long notificationId);

    /**
     * Toplu okundu işaretle
     */
    void markMultipleAsRead(List<Long> notificationIds);

    /**
     * Tüm bildirimleri okundu olarak işaretle
     */
    void markAllAsRead(Long customerId);

    /**
     * Bildirimi sil
     */
    void deleteNotification(Long notificationId);

    /**
     * Toplu bildirim sil
     */
    void deleteMultipleNotifications(List<Long> notificationIds);

    /**
     * ID'ye göre getir
     */
    Notification getNotificationById(Long notificationId);

    /**
     * Tüm bildirimleri getir (sayfalı)
     */
    Page<Notification> getAllNotifications(Pageable pageable);

    /**
     * Okunmamış bildirimleri getir
     */
    List<Notification> getUnreadNotifications();

    /**
     * Okunmamış bildirimleri getir (sayfalı)
     */
    Page<Notification> getUnreadNotifications(Pageable pageable);

    /**
     * Müşteriye göre bildirimleri getir
     */
    List<Notification> getCustomerNotifications(Long customerId);

    /**
     * Müşteriye göre bildirimleri getir (sayfalı)
     */
    Page<Notification> getCustomerNotifications(Long customerId, Pageable pageable);

    /**
     * Müşterinin okunmamış bildirimleri
     */
    List<Notification> getCustomerUnreadNotifications(Long customerId);

    /**
     * Kritik bildirimleri getir
     */
    List<Notification> getCriticalNotifications();

    /**
     * Filtreleme ile bildirimleri getir
     */
    Page<Notification> getNotificationsByFilter(NotificationFilterDTO filter, Pageable pageable);

    /**
     * Okunmamış bildirim sayısı
     */
    long getUnreadCount();

    /**
     * Müşterinin okunmamış bildirim sayısı
     */
    long getCustomerUnreadCount(Long customerId);

    /**
     * Kritik okunmamış bildirim sayısı
     */
    long getCriticalUnreadCount();

    /**
     * Son 10 bildirimi getir
     */
    List<Notification> getRecentNotifications();

    /**
     * Müşterinin son 5 bildirimini getir
     */
    List<Notification> getCustomerRecentNotifications(Long customerId);
}