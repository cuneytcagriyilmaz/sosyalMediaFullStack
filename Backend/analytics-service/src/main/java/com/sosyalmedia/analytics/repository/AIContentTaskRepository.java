// src/main/java/com/sosyalmedia/analytics/repository/AIContentTaskRepository.java

package com.sosyalmedia.analytics.repository;

import com.sosyalmedia.analytics.entity.AIContentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIContentTaskRepository extends JpaRepository<AIContentTask, Long> {

    // Customer ID'ye göre tüm task'ları getir
    List<AIContentTask> findByCustomerId(Long customerId);

    // Customer ID ve status'e göre filtrele
    List<AIContentTask> findByCustomerIdAndStatus(Long customerId, AIContentTask.TaskStatus status);

    // Customer ID'ye göre task sayısını getir
    long countByCustomerId(Long customerId);

    // Belirli bir status'teki tüm task'ları getir
    List<AIContentTask> findByStatus(AIContentTask.TaskStatus status);
}