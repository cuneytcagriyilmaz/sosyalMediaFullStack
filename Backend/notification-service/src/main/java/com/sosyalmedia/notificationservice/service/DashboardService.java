package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.response.DashboardSummaryDTO;

public interface DashboardService {

    /**
     * Dashboard özet bilgilerini getir (Genel)
     */
    DashboardSummaryDTO getDashboardSummary();

    /**
     * Müşteriye özel dashboard bilgilerini getir
     */
    DashboardSummaryDTO getCustomerDashboardSummary(Long customerId);
}