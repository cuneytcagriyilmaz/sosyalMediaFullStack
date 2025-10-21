// src/main/java/com/sosyalmedia/analytics/controller/AIContentTaskController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.dto.AIContentTaskDTO;
import com.sosyalmedia.analytics.dto.response.ApiResponse;
import com.sosyalmedia.analytics.entity.AIContentTask;
import com.sosyalmedia.analytics.service.AIContentTaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/ai-tasks")
@RequiredArgsConstructor
@Slf4j
public class AIContentTaskController {

    private final AIContentTaskService aiContentTaskService;

    /**
     * Yeni AI task oluştur
     * POST /api/v1/analytics/ai-tasks
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AIContentTaskDTO>> createTask(
            @RequestBody AIContentTaskDTO taskDTO) {

        log.info("POST /api/v1/analytics/ai-tasks - Creating AI task");

        AIContentTaskDTO created = aiContentTaskService.createTask(taskDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("AI task oluşturuldu", created));
    }

    /**
     * ID'ye göre task getir
     * GET /api/v1/analytics/ai-tasks/{id}
     */
    @GetMapping("/{id}")

    public ResponseEntity<ApiResponse<AIContentTaskDTO>> getTaskById(
            @PathVariable Long id) {

        log.info("GET /api/v1/analytics/ai-tasks/{}", id);

        AIContentTaskDTO task = aiContentTaskService.getTaskById(id);

        return ResponseEntity.ok(
                ApiResponse.success("AI task getirildi", task)
        );
    }

    /**
     * Müşterinin tüm AI task'larını getir
     * GET /api/v1/analytics/ai-tasks/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AIContentTaskDTO>>> getTasksByCustomer(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/analytics/ai-tasks/customer/{}", customerId);

        List<AIContentTaskDTO> tasks = aiContentTaskService.getTasksByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("AI task'lar getirildi", tasks)
        );
    }

    /**
     * Status'e göre task'ları getir
     * GET /api/v1/analytics/ai-tasks/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<AIContentTaskDTO>>> getTasksByStatus(
            @PathVariable AIContentTask.TaskStatus status) {

        log.info("GET /api/v1/analytics/ai-tasks/status/{}", status);

        List<AIContentTaskDTO> tasks = aiContentTaskService.getTasksByStatus(status);

        return ResponseEntity.ok(
                ApiResponse.success("AI task'lar getirildi", tasks)
        );
    }

    /**
     * AI task güncelle
     * PUT /api/v1/analytics/ai-tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AIContentTaskDTO>> updateTask(
            @PathVariable Long id,
            @RequestBody AIContentTaskDTO taskDTO) {

        log.info("PUT /api/v1/analytics/ai-tasks/{}", id);

        AIContentTaskDTO updated = aiContentTaskService.updateTask(id, taskDTO);

        return ResponseEntity.ok(
                ApiResponse.success("AI task güncellendi", updated)
        );
    }

    /**
     * AI task sil
     * DELETE /api/v1/analytics/ai-tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id) {

        log.info("DELETE /api/v1/analytics/ai-tasks/{}", id);

        aiContentTaskService.deleteTask(id);

        return ResponseEntity.ok(
                ApiResponse.success("AI task silindi", null)
        );
    }

    /**
     * Müşterinin task sayısını getir
     * GET /api/v1/analytics/ai-tasks/customer/{customerId}/count
     */
    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<ApiResponse<Long>> getTasksCount(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/analytics/ai-tasks/customer/{}/count", customerId);

        long count = aiContentTaskService.countTasksByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Task sayısı getirildi", count)
        );
    }
}