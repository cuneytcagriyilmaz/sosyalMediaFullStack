package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.entity.TurkishHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurkishHolidayRepository extends JpaRepository<TurkishHoliday, Long> {

    /**
     * Yıla göre tatilleri getir
     */
    List<TurkishHoliday> findByYear(Integer year);

    /**
     * Tarih aralığındaki tatilleri getir
     */
    @Query("""
        SELECT h FROM TurkishHoliday h 
        WHERE h.year = :year 
        AND h.date BETWEEN :startDate AND :endDate
        ORDER BY h.date ASC
    """)
    List<TurkishHoliday> findByYearAndDateBetween(
            @Param("year") Integer year,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Belirli tarihteki tatili getir
     */
    Optional<TurkishHoliday> findByDate(LocalDate date);

    /**
     * Yıl cache'lenmiş mi kontrol et
     */
    boolean existsByYear(Integer year);
}