// src/main/java/com/sosyalmedia/analytics/repository/CustomerNoteRepository.java

package com.sosyalmedia.analytics.repository;

import com.sosyalmedia.analytics.entity.CustomerNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerNoteRepository extends JpaRepository<CustomerNote, Long> {

    // Customer ID'ye göre tüm notları getir (en yeni önce)
    List<CustomerNote> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // Customer ID'ye göre not sayısı
    long countByCustomerId(Long customerId);
}