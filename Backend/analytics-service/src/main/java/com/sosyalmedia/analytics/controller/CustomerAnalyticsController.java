// src/main/java/com/sosyalmedia/analytics/controller/CustomerAnalyticsController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.dto.*;
import com.sosyalmedia.analytics.dto.ApiResponse;
import com.sosyalmedia.analytics.service.AIContentTaskService;
import com.sosyalmedia.analytics.service.CustomerAnalyticsService;
import com.sosyalmedia.analytics.service.CustomerNoteService;
import com.sosyalmedia.analytics.service.OnboardingTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerAnalyticsController {

    private final CustomerAnalyticsService customerAnalyticsService;
    private final AIContentTaskService aiContentTaskService;
    private final OnboardingTaskService onboardingTaskService;
    private final CustomerNoteService customerNoteService;

    // ==================== CUSTOMER DETAIL ====================

    /**
     * Müşteri detay bilgilerini getir
     * GET /api/v1/analytics/customers/{customerId}/detail
     */
    @GetMapping("/{customerId}/detail")
    public ResponseEntity<ApiResponse<CustomerDetailDTO>> getCustomerDetail(
            @PathVariable Long customerId
    ) {
        log.info("GET /api/v1/analytics/customers/{}/detail", customerId);

        CustomerDetailDTO detail = customerAnalyticsService.getCustomerDetail(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Customer detail fetched successfully", detail)
        );
    }

    /**
     * Müşteri post istatistiklerini getir
     * GET /api/v1/analytics/customers/{customerId}/post-stats
     */
    @GetMapping("/{customerId}/post-stats")
    public ResponseEntity<ApiResponse<PostStatsDTO>> getCustomerPostStats(
            @PathVariable Long customerId
    ) {
        log.info("GET /api/v1/analytics/customers/{}/post-stats", customerId);

        PostStatsDTO stats = customerAnalyticsService.getCustomerPostStats(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Post statistics fetched successfully", stats)
        );
    }

    /**
     * Müşterinin yaklaşan postlarını getir
     * GET /api/v1/analytics/customers/{customerId}/upcoming-posts?limit=5
     */
    @GetMapping("/{customerId}/upcoming-posts")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getUpcomingPosts(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        log.info("GET /api/v1/analytics/customers/{}/upcoming-posts?limit={}", customerId, limit);

        List<PostDTO> posts = customerAnalyticsService.getCustomerUpcomingPosts(customerId, limit);

        return ResponseEntity.ok(
                ApiResponse.success("Upcoming posts fetched successfully", posts)
        );
    }

    /**
     * Müşteri aktivitelerini getir
     * GET /api/v1/analytics/customers/{customerId}/activities?limit=10
     */
    @GetMapping("/{customerId}/activities")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getCustomerActivities(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("GET /api/v1/analytics/customers/{}/activities?limit={}", customerId, limit);

        List<ActivityLogDTO> activities = customerAnalyticsService.getCustomerActivities(customerId, limit);

        return ResponseEntity.ok(
                ApiResponse.success("Customer activities fetched successfully", activities)
        );
    }

    // ==================== AI CONTENT TASKS ====================

    /**
     * Müşterinin AI içerik görevlerini getir
     * GET /api/v1/analytics/customers/{customerId}/ai-tasks
     */
    @GetMapping("/{customerId}/ai-tasks")
    public ResponseEntity<ApiResponse<List<AIContentTaskDTO>>> getAIContentTasks(
            @PathVariable Long customerId
    ) {
        log.info("GET /api/v1/analytics/customers/{}/ai-tasks", customerId);

        List<AIContentTaskDTO> tasks = aiContentTaskService.getTasksByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("AI content tasks fetched successfully", tasks)
        );
    }

    /**
     * AI içerik görevini güncelle
     * PATCH /api/v1/analytics/ai-tasks/{taskId}
     */
    @PatchMapping("/ai-tasks/{taskId}")
    public ResponseEntity<ApiResponse<AIContentTaskDTO>> updateAITask(
            @PathVariable Long taskId,
            @RequestBody AIContentTaskDTO taskDTO
    ) {
        log.info("PATCH /api/v1/analytics/ai-tasks/{}", taskId);

        AIContentTaskDTO updatedTask = aiContentTaskService.updateTask(taskId, taskDTO);

        return ResponseEntity.ok(
                ApiResponse.success("AI content task updated successfully", updatedTask)
        );
    }

    // ==================== ONBOARDING TASKS ====================

    /**
     * Müşterinin onboarding görevlerini getir
     * GET /api/v1/analytics/customers/{customerId}/onboarding-tasks
     */
    @GetMapping("/{customerId}/onboarding-tasks")
    public ResponseEntity<ApiResponse<List<OnboardingTaskDTO>>> getOnboardingTasks(
            @PathVariable Long customerId
    ) {
        log.info("GET /api/v1/analytics/customers/{}/onboarding-tasks", customerId);

        List<OnboardingTaskDTO> tasks = onboardingTaskService.getTasksByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding tasks fetched successfully", tasks)
        );
    }

    /**
     * Onboarding görevini güncelle
     * PATCH /api/v1/analytics/onboarding-tasks/{taskId}
     */
    @PatchMapping("/onboarding-tasks/{taskId}")
    public ResponseEntity<ApiResponse<OnboardingTaskDTO>> updateOnboardingTask(
            @PathVariable Long taskId,
            @RequestBody OnboardingTaskDTO taskDTO
    ) {
        log.info("PATCH /api/v1/analytics/onboarding-tasks/{}", taskId);

        OnboardingTaskDTO updatedTask = onboardingTaskService.updateTask(taskId, taskDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Onboarding task updated successfully", updatedTask)
        );
    }

    // ==================== CUSTOMER NOTES ====================

    /**
     * Müşteri notlarını getir
     * GET /api/v1/analytics/customers/{customerId}/notes
     */
    @GetMapping("/{customerId}/notes")
    public ResponseEntity<ApiResponse<List<CustomerNoteDTO>>> getCustomerNotes(
            @PathVariable Long customerId
    ) {
        log.info("GET /api/v1/analytics/customers/{}/notes", customerId);

        List<CustomerNoteDTO> notes = customerNoteService.getNotesByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Customer notes fetched successfully", notes)
        );
    }

    /**
     * Müşteriye not ekle
     * POST /api/v1/analytics/customers/{customerId}/notes
     */
    @PostMapping("/{customerId}/notes")
    public ResponseEntity<ApiResponse<CustomerNoteDTO>> addCustomerNote(
            @PathVariable Long customerId,
            @RequestBody CustomerNoteDTO noteDTO
    ) {
        log.info("POST /api/v1/analytics/customers/{}/notes", customerId);

        noteDTO.setCustomerId(customerId);
        CustomerNoteDTO createdNote = customerNoteService.createNote(noteDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Customer note added successfully", createdNote)
        );
    }
}