// src/main/java/com/sosyalmedia/analytics/service/CustomerAnalyticsService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.*;

import java.util.List;

public interface CustomerAnalyticsService {

    /**
     * Müşteri detay bilgilerini getir
     */
    CustomerDetailDTO getCustomerDetail(Long customerId);

    /**
     * Müşteri post istatistiklerini getir
     */
    PostStatsDTO getCustomerPostStats(Long customerId);

    /**
     * Müşterinin yaklaşan postlarını getir
     */
    List<PostDTO> getCustomerUpcomingPosts(Long customerId, int limit);

    /**
     * Müşteri aktivitelerini getir
     */
    List<ActivityLogDTO> getCustomerActivities(Long customerId, int limit);

    /**
     * Müşteri notlarını getir
     */
    List<CustomerNoteDTO> getCustomerNotes(Long customerId);
}