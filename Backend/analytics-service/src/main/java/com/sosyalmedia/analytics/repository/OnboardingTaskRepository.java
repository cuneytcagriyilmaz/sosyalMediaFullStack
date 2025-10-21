// src/main/java/com/sosyalmedia/analytics/repository/OnboardingTaskRepository.java

package com.sosyalmedia.analytics.repository;

import com.sosyalmedia.analytics.entity.OnboardingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnboardingTaskRepository extends JpaRepository<OnboardingTask, Long> {

    // Customer ID'ye göre tüm task'ları getir
    List<OnboardingTask> findByCustomerId(Long customerId);

    // Customer ID ve platform'a göre filtrele
    List<OnboardingTask> findByCustomerIdAndPlatform(Long customerId, OnboardingTask.Platform platform);

    // Customer ID ve status'e göre filtrele
    List<OnboardingTask> findByCustomerIdAndStatus(Long customerId, OnboardingTask.TaskStatus status);

    // Customer ID'ye göre tamamlanan task sayısı
    long countByCustomerIdAndStatus(Long customerId, OnboardingTask.TaskStatus status);
}