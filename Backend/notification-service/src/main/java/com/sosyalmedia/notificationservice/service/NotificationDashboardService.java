package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.response.NotificationDashboardResponse;

public interface NotificationDashboardService {

    /**
     * Bildiri Dashboard'u için tüm verileri getirir
     * - Son 7 gündeki firmalar ve deadline'ları
     * - Kritik ve gecikmiş deadline'lar
     * - Genel istatistikler
     */
    NotificationDashboardResponse getDashboardData();

    /**
     * Özel gün sayısı ile dashboard verisi (varsayılan 7)
     */
    NotificationDashboardResponse getDashboardData(int days);
}