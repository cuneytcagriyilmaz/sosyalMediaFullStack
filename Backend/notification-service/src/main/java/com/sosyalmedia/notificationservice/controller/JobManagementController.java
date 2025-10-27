package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.service.job.CheckOverduePostsJob;
import com.sosyalmedia.notificationservice.service.job.CheckSpecialDatesJob;
import com.sosyalmedia.notificationservice.service.job.CheckUpcomingPostsJob;
import com.sosyalmedia.notificationservice.service.job.SendEmailNotificationsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
 public class JobManagementController {

    private final CheckUpcomingPostsJob checkUpcomingPostsJob;
    private final CheckOverduePostsJob checkOverduePostsJob;
    private final CheckSpecialDatesJob checkSpecialDatesJob;
    private final SendEmailNotificationsJob sendEmailNotificationsJob;

    // ==================== MANUAL JOB EXECUTION ====================

    /**
     * Yaklaşan postları kontrol et (Manuel)
     * POST /api/jobs/check-upcoming-posts/run
     */
    @PostMapping("/check-upcoming-posts/run")
    public ResponseEntity<JobExecutionResponse> runCheckUpcomingPostsJob() {
        log.info("🚀 CheckUpcomingPostsJob manuel olarak çalıştırılıyor...");

        try {
            long startTime = System.currentTimeMillis();
            checkUpcomingPostsJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("CheckUpcomingPostsJob")
                    .status("SUCCESS")
                    .message("Yaklaşan postlar kontrolü tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ CheckUpcomingPostsJob hatası: {}", e.getMessage(), e);

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("CheckUpcomingPostsJob")
                    .status("FAILED")
                    .message("Hata: " + e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Gecikmiş postları kontrol et (Manuel)
     * POST /api/jobs/check-overdue-posts/run
     */
    @PostMapping("/check-overdue-posts/run")
    public ResponseEntity<JobExecutionResponse> runCheckOverduePostsJob() {
        log.info("🚀 CheckOverduePostsJob manuel olarak çalıştırılıyor...");

        try {
            long startTime = System.currentTimeMillis();
            checkOverduePostsJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("CheckOverduePostsJob")
                    .status("SUCCESS")
                    .message("Gecikmiş postlar kontrolü tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ CheckOverduePostsJob hatası: {}", e.getMessage(), e);

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("CheckOverduePostsJob")
                    .status("FAILED")
                    .message("Hata: " + e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Özel günleri kontrol et (Manuel)
     * POST /api/jobs/check-special-dates/run
     */
    @PostMapping("/check-special-dates/run")
    public ResponseEntity<JobExecutionResponse> runCheckSpecialDatesJob() {
        log.info("🚀 CheckSpecialDatesJob manuel olarak çalıştırılıyor...");

        try {
            long startTime = System.currentTimeMillis();
            checkSpecialDatesJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("CheckSpecialDatesJob")
                    .status("SUCCESS")
                    .message("Özel günler kontrolü tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ CheckSpecialDatesJob hatası: {}", e.getMessage(), e);

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("CheckSpecialDatesJob")
                    .status("FAILED")
                    .message("Hata: " + e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * E-posta bildirimlerini gönder (Manuel)
     * POST /api/jobs/send-email-notifications/run
     */
    @PostMapping("/send-email-notifications/run")
    public ResponseEntity<JobExecutionResponse> runSendEmailNotificationsJob() {
        log.info("🚀 SendEmailNotificationsJob manuel olarak çalıştırılıyor...");

        try {
            long startTime = System.currentTimeMillis();
            sendEmailNotificationsJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("SendEmailNotificationsJob")
                    .status("SUCCESS")
                    .message("E-posta bildirimleri gönderildi")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ SendEmailNotificationsJob hatası: {}", e.getMessage(), e);

            JobExecutionResponse response = JobExecutionResponse.builder()
                    .jobName("SendEmailNotificationsJob")
                    .status("FAILED")
                    .message("Hata: " + e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Tüm job'ları çalıştır (Manuel)
     * POST /api/jobs/run-all
     */
    @PostMapping("/run-all")
    public ResponseEntity<Map<String, JobExecutionResponse>> runAllJobs() {
        log.info("🚀 Tüm job'lar manuel olarak çalıştırılıyor...");

        Map<String, JobExecutionResponse> results = new HashMap<>();

        // 1. CheckUpcomingPostsJob
        try {
            long startTime = System.currentTimeMillis();
            checkUpcomingPostsJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            results.put("CheckUpcomingPostsJob", JobExecutionResponse.builder()
                    .jobName("CheckUpcomingPostsJob")
                    .status("SUCCESS")
                    .message("Tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build());
        } catch (Exception e) {
            results.put("CheckUpcomingPostsJob", JobExecutionResponse.builder()
                    .jobName("CheckUpcomingPostsJob")
                    .status("FAILED")
                    .message(e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build());
        }

        // 2. CheckOverduePostsJob
        try {
            long startTime = System.currentTimeMillis();
            checkOverduePostsJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            results.put("CheckOverduePostsJob", JobExecutionResponse.builder()
                    .jobName("CheckOverduePostsJob")
                    .status("SUCCESS")
                    .message("Tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build());
        } catch (Exception e) {
            results.put("CheckOverduePostsJob", JobExecutionResponse.builder()
                    .jobName("CheckOverduePostsJob")
                    .status("FAILED")
                    .message(e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build());
        }

        // 3. CheckSpecialDatesJob
        try {
            long startTime = System.currentTimeMillis();
            checkSpecialDatesJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            results.put("CheckSpecialDatesJob", JobExecutionResponse.builder()
                    .jobName("CheckSpecialDatesJob")
                    .status("SUCCESS")
                    .message("Tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build());
        } catch (Exception e) {
            results.put("CheckSpecialDatesJob", JobExecutionResponse.builder()
                    .jobName("CheckSpecialDatesJob")
                    .status("FAILED")
                    .message(e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build());
        }

        // 4. SendEmailNotificationsJob
        try {
            long startTime = System.currentTimeMillis();
            sendEmailNotificationsJob.execute(null);
            long duration = System.currentTimeMillis() - startTime;

            results.put("SendEmailNotificationsJob", JobExecutionResponse.builder()
                    .jobName("SendEmailNotificationsJob")
                    .status("SUCCESS")
                    .message("Tamamlandı")
                    .executedAt(LocalDateTime.now())
                    .durationMs(duration)
                    .build());
        } catch (Exception e) {
            results.put("SendEmailNotificationsJob", JobExecutionResponse.builder()
                    .jobName("SendEmailNotificationsJob")
                    .status("FAILED")
                    .message(e.getMessage())
                    .executedAt(LocalDateTime.now())
                    .build());
        }

        return ResponseEntity.ok(results);
    }

    /**
     * Job listesini getir
     * GET /api/jobs/list
     */
    @GetMapping("/list")
    public ResponseEntity<JobListResponse> getJobList() {
        log.info("📋 Job listesi getiriliyor");

        JobListResponse response = JobListResponse.builder()
                .totalJobs(4)
                .jobs(java.util.Arrays.asList(
                        JobInfo.builder()
                                .jobName("CheckUpcomingPostsJob")
                                .description("Yaklaşan postları kontrol eder ve bildirim gönderir")
                                .cronExpression("0 0 9 * * ?")
                                .build(),
                        JobInfo.builder()
                                .jobName("CheckOverduePostsJob")
                                .description("Gecikmiş postları kontrol eder")
                                .cronExpression("0 0 10 * * ?")
                                .build(),
                        JobInfo.builder()
                                .jobName("CheckSpecialDatesJob")
                                .description("Yaklaşan özel günleri kontrol eder")
                                .cronExpression("0 0 8 * * ?")
                                .build(),
                        JobInfo.builder()
                                .jobName("SendEmailNotificationsJob")
                                .description("Bekleyen e-posta bildirimlerini gönderir")
                                .cronExpression("0 */5 * * * ?")
                                .build()
                ))
                .build();

        return ResponseEntity.ok(response);
    }

    // ==================== INNER CLASSES ====================

    @lombok.Data
    @lombok.Builder
    public static class JobExecutionResponse {
        private String jobName;
        private String status;
        private String message;
        private LocalDateTime executedAt;
        private Long durationMs;
    }

    @lombok.Data
    @lombok.Builder
    public static class JobListResponse {
        private Integer totalJobs;
        private java.util.List<JobInfo> jobs;
    }

    @lombok.Data
    @lombok.Builder
    public static class JobInfo {
        private String jobName;
        private String description;
        private String cronExpression;
    }
}