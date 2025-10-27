package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;

import java.util.List;

public interface CalendarificService {

    /**
     * Calendarific API'den tatilleri çek ve kaydet
     */
    void syncHolidays(int year);

    /**
     * Otomatik senkronizasyon (Her yıl 1 Ocak'ta)
     */
    void autoSyncHolidays();

    /**
     * İlk başlatmada senkronizasyon
     */
    void initSync();

    /**
     * Önümüzdeki N gün içindeki tatilleri getir
     */
    List<GlobalSpecialDate> getUpcomingHolidays(int daysAhead);

    /**
     * Belirli bir yıldaki tatilleri getir
     */
    List<GlobalSpecialDate> getHolidaysByYear(int year);

    /**
     * Bugünden sonraki tüm tatilleri getir
     */
    List<GlobalSpecialDate> getAllUpcomingHolidays();
}