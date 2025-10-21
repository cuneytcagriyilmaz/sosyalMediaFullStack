// src/main/java/com/sosyalmedia/analytics/controller/OnboardingTaskController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.dto.OnboardingTaskDTO;
import com.sosyalmedia.analytics.dto.response.ApiResponse;
import com.sosyalmedia.analytics.entity.OnboardingTask;
import com.sosyalmedia.analytics.service.OnboardingTaskService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/onboarding-tasks")
@RequiredArgsConstructor
@Slf4j
public class OnboardingTaskController {

    private final OnboardingTaskService onboardingTaskService;

    /**
     * Yeni onboarding task oluştur
     * POST /api/v1/analytics/onboarding-tasks
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OnboardingTaskDTO>> createTask(
            @RequestBody OnboardingTaskDTO taskDTO) {

        log.info("POST /api/v1/analytics/onboarding-tasks - Creating task");

        OnboardingTaskDTO created = onboardingTaskService.createTask(taskDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Onboarding task oluşturuldu", created));
    }

    /**
     * ID'ye göre task getir
     * GET /api/v1/analytics/onboarding-tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OnboardingTaskDTO>> getTaskById(
            @PathVariable Long id) {

        log.info("GET /api/v1/analytics/onboarding-tasks/{}", id);

        OnboardingTaskDTO task = onboardingTaskService.getTaskById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding task getirildi", task)
        );
    }

    /**
     * Müşterinin tüm onboarding task'larını getir
     * GET /api/v1/analytics/onboarding-tasks/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OnboardingTaskDTO>>> getTasksByCustomer(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/analytics/onboarding-tasks/customer/{}", customerId);

        List<OnboardingTaskDTO> tasks = onboardingTaskService.getTasksByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding task'lar getirildi", tasks)
        );
    }

    /**
     * Platform'a göre task'ları getir
     * GET /api/v1/analytics/onboarding-tasks/customer/{customerId}/platform/{platform}
     */
    @GetMapping("/customer/{customerId}/platform/{platform}")
    public ResponseEntity<ApiResponse<List<OnboardingTaskDTO>>> getTasksByPlatform(
            @PathVariable Long customerId,
            @PathVariable OnboardingTask.Platform platform) {

        log.info("GET /api/v1/analytics/onboarding-tasks/customer/{}/platform/{}", customerId, platform);

        List<OnboardingTaskDTO> tasks = onboardingTaskService.getTasksByCustomerIdAndPlatform(customerId, platform);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding task'lar getirildi", tasks)
        );
    }

    /**
     * Onboarding task güncelle
     * PUT /api/v1/analytics/onboarding-tasks/{id}
     */
    @PutMapping("/{id}")

    public ResponseEntity<ApiResponse<OnboardingTaskDTO>> updateTask(
            @PathVariable Long id,
            @RequestBody OnboardingTaskDTO taskDTO) {

        log.info("PUT /api/v1/analytics/onboarding-tasks/{}", id);

        OnboardingTaskDTO updated = onboardingTaskService.updateTask(id, taskDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding task güncellendi", updated)
        );
    }

    /**
     * Onboarding task sil
     * DELETE /api/v1/analytics/onboarding-tasks/{id}
     */
    @DeleteMapping("/{id}")

    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id) {

        log.info("DELETE /api/v1/analytics/onboarding-tasks/{}", id);

        onboardingTaskService.deleteTask(id);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding task silindi", null)
        );
    }

    /**
     * Müşterinin tamamlanmış task sayısını getir
     * GET /api/v1/analytics/onboarding-tasks/customer/{customerId}/completed-count
     */
    @GetMapping("/customer/{customerId}/completed-count")
    public ResponseEntity<ApiResponse<Long>> getCompletedTasksCount(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/analytics/onboarding-tasks/customer/{}/completed-count", customerId);

        long count = onboardingTaskService.countCompletedTasksByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Tamamlanmış task sayısı getirildi", count)
        );
    }
}