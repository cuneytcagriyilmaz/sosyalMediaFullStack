package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GlobalSpecialDateRepository extends JpaRepository<GlobalSpecialDate, Long> {

    /**
     * Tarih aralığındaki özel günleri getir
     */
    List<GlobalSpecialDate> findByDateValueBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tarih aralığındaki özel günleri getir (sıralı)
     */
    List<GlobalSpecialDate> findByDateValueBetweenOrderByDateValueAsc(LocalDate startDate, LocalDate endDate);

    /**
     * İsim ve tarihe göre bul (Duplicate kontrolü için)
     */
    Optional<GlobalSpecialDate> findByDateNameAndDateValue(String dateName, LocalDate dateValue);

    /**
     * Tarihe göre özel gün var mı kontrol et
     */
    Optional<GlobalSpecialDate> findByDateValue(LocalDate dateValue);

    /**
     * Tipe göre getir
     */
    List<GlobalSpecialDate> findByDateType(String dateType);

    /**
     * Tipe ve tarih aralığına göre getir
     */
    List<GlobalSpecialDate> findByDateTypeAndDateValueBetween(
            String dateType,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Aktif özel günleri getir
     */
    List<GlobalSpecialDate> findByAutoSuggestPostTrue();

    /**
     * Tarih aralığındaki kayıt sayısı
     */
    long countByDateValueBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Setting key ile var mı kontrolü
     */
    boolean existsByDateNameAndDateValue(String dateName, LocalDate dateValue);

    /**
     * Yıla göre getir
     */
    @Query("SELECT g FROM GlobalSpecialDate g WHERE YEAR(g.dateValue) = :year ORDER BY g.dateValue")
    List<GlobalSpecialDate> findByYear(@Param("year") int year);

    /**
     * Yaklaşan özel günler (bugünden sonra)
     */
    @Query("SELECT g FROM GlobalSpecialDate g WHERE g.dateValue >= :today ORDER BY g.dateValue")
    List<GlobalSpecialDate> findUpcomingDates(@Param("today") LocalDate today);

    /**
     * Sektöre uygun özel günleri getir
     */
    @Query("SELECT g FROM GlobalSpecialDate g WHERE g.applicableSectors IS NULL OR g.applicableSectors LIKE %:sector%")
    List<GlobalSpecialDate> findBySectorOrAll(@Param("sector") String sector);
}