package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.entity.PostDeadlineArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostDeadlineArchiveRepository extends JpaRepository<PostDeadlineArchive, Long> {

    // Customer'a göre arşiv
    List<PostDeadlineArchive> findByCustomerIdOrderByArchivedAtDesc(Long customerId);

    // Tüm arşiv (en yeni en üstte)
    List<PostDeadlineArchive> findAllByOrderByArchivedAtDesc();

    // Belirli tarih aralığındaki arşiv
    List<PostDeadlineArchive> findByArchivedAtBetweenOrderByArchivedAtDesc(
            LocalDateTime startDate, LocalDateTime endDate);

    // Original deadline ID'ye göre
    Optional<PostDeadlineArchive> findByOriginalDeadlineId(Long originalDeadlineId);

    // Sebebe göre
    List<PostDeadlineArchive> findByArchivedReasonOrderByArchivedAtDesc(String reason);

    // İstatistik: Toplam arşiv sayısı
    long count();

    // İstatistik: Son 30 gün arşivlenen
    @Query("SELECT COUNT(a) FROM PostDeadlineArchive a WHERE a.archivedAt >= :since")
    long countArchivedSince(@Param("since") LocalDateTime since);
}