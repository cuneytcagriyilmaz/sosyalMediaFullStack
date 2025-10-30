package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineArchiveResponse;
import com.sosyalmedia.notificationservice.service.PostDeadlineArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications/archive")
@RequiredArgsConstructor
@Slf4j
public class PostDeadlineArchiveController {

    private final PostDeadlineArchiveService archiveService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDeadlineArchiveResponse>>> getAllArchivedDeadlines() {
        log.info("📋 REST: GET /api/v1/notifications/archive");

        List<PostDeadlineArchiveResponse> archives = archiveService.getAllArchivedDeadlines();

        return ResponseEntity.ok(ApiResponse.success(archives));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<PostDeadlineArchiveResponse>>> getArchivedDeadlinesByCustomer(
            @PathVariable Long customerId) {

        log.info("👤 REST: GET /api/v1/notifications/archive/customer/{}", customerId);

        List<PostDeadlineArchiveResponse> archives = archiveService.getArchivedDeadlinesByCustomer(customerId);

        return ResponseEntity.ok(ApiResponse.success(archives));
    }

    @PostMapping("/restore/{archiveId}")
    public ResponseEntity<ApiResponse<Void>> restoreDeadline(@PathVariable Long archiveId) {
        log.info("♻️ REST: POST /api/v1/notifications/archive/restore/{}", archiveId);

        archiveService.restoreDeadline(archiveId);

        return ResponseEntity.ok(ApiResponse.success("Deadline arşivden geri yüklendi", null));
    }

    @DeleteMapping("/{archiveId}")
    public ResponseEntity<ApiResponse<Void>> deleteArchive(@PathVariable Long archiveId) {
        log.info("🗑️ REST: DELETE /api/v1/notifications/archive/{}", archiveId);

        archiveService.deleteArchive(archiveId);

        return ResponseEntity.ok(ApiResponse.success("Arşiv kalıcı olarak silindi", null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getArchiveStats() {
        log.info("📊 REST: GET /api/v1/notifications/archive/stats");

        Map<String, Long> stats = Map.of(
                "totalArchived", archiveService.getTotalArchivedCount(),
                "archivedLast30Days", archiveService.getArchivedCountLast30Days()
        );

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}