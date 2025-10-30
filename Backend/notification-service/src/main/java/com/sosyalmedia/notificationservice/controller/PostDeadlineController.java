package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.request.PostDeadlineCreateRequest;
import com.sosyalmedia.notificationservice.dto.request.PostDeadlineUpdateRequest;
import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.dto.response.CompanyWithDeadlinesDTO;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineResponse;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineStatsResponse;
import com.sosyalmedia.notificationservice.service.PostDeadlineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications/post-deadlines")
@RequiredArgsConstructor
@Slf4j
public class PostDeadlineController {

    private final PostDeadlineService postDeadlineService;


    @PostMapping
    public ResponseEntity<ApiResponse<PostDeadlineResponse>> createDeadline(
            @Valid @RequestBody PostDeadlineCreateRequest request) {

        log.info("📝 REST: POST /api/v1/notifications/post-deadlines - Customer: {}",
                request.getCustomerId());

        PostDeadlineResponse response = postDeadlineService.createDeadline(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post deadline başarıyla oluşturuldu", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDeadlineResponse>>> getAllDeadlines() {
        log.info("📋 REST: GET /api/v1/notifications/post-deadlines");

        List<PostDeadlineResponse> deadlines = postDeadlineService.getAllDeadlines();

        return ResponseEntity.ok(ApiResponse.success(deadlines));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDeadlineResponse>> getDeadlineById(
            @PathVariable Long id) {

        log.info("🔍 REST: GET /api/v1/notifications/post-deadlines/{}", id);

        PostDeadlineResponse deadline = postDeadlineService.getDeadlineById(id);

        return ResponseEntity.ok(ApiResponse.success(deadline));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<PostDeadlineResponse>>> getUpcomingDeadlines(
            @RequestParam(defaultValue = "7") int days) {

        log.info("📅 REST: GET /api/v1/notifications/post-deadlines/upcoming?days={}", days);

        List<PostDeadlineResponse> deadlines = postDeadlineService.getUpcomingDeadlines(days);

        return ResponseEntity.ok(ApiResponse.success(deadlines));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<PostDeadlineResponse>>> getDeadlinesByCustomer(
            @PathVariable Long customerId) {

        log.info("👤 REST: GET /api/v1/notifications/post-deadlines/customer/{}", customerId);

        List<PostDeadlineResponse> deadlines = postDeadlineService.getDeadlinesByCustomerId(customerId);

        return ResponseEntity.ok(ApiResponse.success(deadlines));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDeadlineResponse>> updateDeadline(
            @PathVariable Long id,
            @Valid @RequestBody PostDeadlineUpdateRequest request) {

        log.info("🔄 REST: PUT /api/v1/notifications/post-deadlines/{}", id);

        PostDeadlineResponse response = postDeadlineService.updateDeadline(id, request);

        return ResponseEntity.ok(ApiResponse.success("Deadline başarıyla güncellendi", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDeadline(
            @PathVariable Long id) {

        log.info("🗑️ REST: DELETE /api/v1/notifications/post-deadlines/{}", id);

        postDeadlineService.deleteDeadline(id);

        return ResponseEntity.ok(ApiResponse.success("Deadline başarıyla silindi", null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<PostDeadlineStatsResponse>> getStatistics() {
        log.info("📊 REST: GET /api/v1/notifications/post-deadlines/stats");

        PostDeadlineStatsResponse stats = postDeadlineService.getStatistics();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PostMapping("/auto-create/{customerId}")
    public ResponseEntity<ApiResponse<PostDeadlineResponse>> autoCreateForNewCustomer(
            @PathVariable Long customerId) {

        log.info("🎉 REST: POST /api/v1/notifications/post-deadlines/auto-create/{}", customerId);

        PostDeadlineResponse response = postDeadlineService.autoCreateForNewCustomer(customerId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("İlk post deadline oluşturuldu (+30 gün)", response));
    }

    @GetMapping("/companies-with-upcoming")
    public ResponseEntity<ApiResponse<List<CompanyWithDeadlinesDTO>>> getCompaniesWithUpcomingDeadlines(
            @RequestParam(defaultValue = "7") int days) {

        log.info("🏢 REST: GET /api/v1/notifications/post-deadlines/companies-with-upcoming?days={}", days);

        List<CompanyWithDeadlinesDTO> companies = postDeadlineService.getCompaniesWithUpcomingDeadlines(days);

        return ResponseEntity.ok(ApiResponse.success(companies));
    }
}