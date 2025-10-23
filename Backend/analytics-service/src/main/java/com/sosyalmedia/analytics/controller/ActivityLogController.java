// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/controller/ActivityLogController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.client.CustomerServiceClient;
import com.sosyalmedia.analytics.client.dto.customerservice.ApiResponseDTO;
import com.sosyalmedia.analytics.client.dto.customerservice.CustomerResponseDTO;
import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.dto.ApiResponse;
import com.sosyalmedia.analytics.service.ActivityLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final CustomerServiceClient customerServiceClient;

    /**
     * Yeni aktivite ekle
     * POST /api/v1/analytics/activities
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ActivityLogDTO>> createActivity(
            @Valid @RequestBody ActivityLogDTO activityDTO) {

        log.info("📝 API: POST /api/v1/analytics/activities - Type: {}, Customer: {}",
                activityDTO.getActivityType(), activityDTO.getCustomerId());

        try {
            // ✅ Customer validation
            if (activityDTO.getCustomerId() != null) {
                validateCustomer(activityDTO.getCustomerId());
            }

            ActivityLogDTO created = activityLogService.createActivity(activityDTO);
            log.info("✅ Aktivite başarıyla oluşturuldu: ID={}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created));

        } catch (CustomerNotFoundException e) {
            log.error("❌ Müşteri bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (IllegalArgumentException e) {
            log.error("❌ Validation hatası: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Aktivite eklenirken hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktivite eklenemedi: " + e.getMessage()));
        }
    }

    /**
     * Toplu aktivite ekle
     * POST /api/v1/analytics/activities/bulk
     */
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> createActivities(
            @Valid @RequestBody List<ActivityLogDTO> activities) {

        log.info("📝 API: POST /api/v1/analytics/activities/bulk - {} adet aktivite", activities.size());

        try {
            // ✅ Tüm customer'ları validate et
            for (ActivityLogDTO dto : activities) {
                if (dto.getCustomerId() != null) {
                    validateCustomer(dto.getCustomerId());
                }
            }

            List<ActivityLogDTO> created = activityLogService.createActivities(activities);
            log.info("✅ {} aktivite başarıyla oluşturuldu", created.size());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created));

        } catch (CustomerNotFoundException e) {
            log.error("❌ Müşteri bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Toplu aktivite eklenirken hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktiviteler eklenemedi: " + e.getMessage()));
        }
    }

    /**
     * Son N aktiviteyi getir
     * GET /api/v1/analytics/activities?limit=50
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getRecentActivities(
            @RequestParam(defaultValue = "50") int limit) {

        log.info("📋 API: GET /api/v1/analytics/activities?limit={}", limit);

        try {
            List<ActivityLogDTO> activities = activityLogService.getRecentActivities(limit);
            return ResponseEntity.ok(ApiResponse.success(activities));
        } catch (Exception e) {
            log.error("❌ Aktiviteler getirilirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktiviteler getirilemedi: " + e.getMessage()));
        }
    }


    /**
     * Aktiviteyi ID ile getir
     * GET /api/v1/analytics/activities/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityLogDTO>> getActivityById(@PathVariable Long id) {
        log.info("📋 API: GET /api/v1/analytics/activities/{}", id);

        try {
            ActivityLogDTO activity = activityLogService.getActivityById(id);
            return ResponseEntity.ok(ApiResponse.success(activity));
        } catch (RuntimeException e) {
            log.error("❌ Aktivite bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Müşteriye ait aktiviteleri getir
     * GET /api/v1/analytics/activities/customer/{customerId}?limit=20
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getCustomerActivities(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "20") int limit) {

        log.info("📋 API: GET /api/v1/analytics/activities/customer/{}?limit={}", customerId, limit);

        try {
            // ✅ Customer validation
            validateCustomer(customerId);

            List<ActivityLogDTO> activities = activityLogService.getCustomerActivities(customerId, limit);
            return ResponseEntity.ok(ApiResponse.success(activities));

        } catch (CustomerNotFoundException e) {
            log.error("❌ Müşteri bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Müşteri aktiviteleri getirilirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktiviteler getirilemedi: " + e.getMessage()));
        }
    }

    /**
     * Aktivite tipine göre filtrele
     * GET /api/v1/analytics/activities/type/{activityType}?limit=50
     */
    @GetMapping("/type/{activityType}")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getActivitiesByType(
            @PathVariable String activityType,
            @RequestParam(defaultValue = "50") int limit) {

        log.info("📋 API: GET /api/v1/analytics/activities/type/{}?limit={}", activityType, limit);

        try {
            List<ActivityLogDTO> activities = activityLogService.getActivitiesByType(activityType, limit);
            return ResponseEntity.ok(ApiResponse.success(activities));

        } catch (IllegalArgumentException e) {
            log.error("❌ Geçersiz aktivite tipi: {}", activityType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Geçersiz aktivite tipi: " + activityType));

        } catch (Exception e) {
            log.error("❌ Aktiviteler getirilirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktiviteler getirilemedi: " + e.getMessage()));
        }
    }

    /**
     * Müşteri ve tip kombinasyonu ile getir
     * GET /api/v1/analytics/activities/customer/{customerId}/type/{activityType}?limit=20
     */
    @GetMapping("/customer/{customerId}/type/{activityType}")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getCustomerActivitiesByType(
            @PathVariable Long customerId,
            @PathVariable String activityType,
            @RequestParam(defaultValue = "20") int limit) {

        log.info("📋 API: GET /api/v1/analytics/activities/customer/{}/type/{}?limit={}",
                customerId, activityType, limit);

        try {
            // ✅ Customer validation
            validateCustomer(customerId);

            List<ActivityLogDTO> activities = activityLogService
                    .getCustomerActivitiesByType(customerId, activityType, limit);
            return ResponseEntity.ok(ApiResponse.success(activities));

        } catch (CustomerNotFoundException e) {
            log.error("❌ Müşteri bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (IllegalArgumentException e) {
            log.error("❌ Geçersiz aktivite tipi: {}", activityType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Geçersiz aktivite tipi: " + activityType));

        } catch (Exception e) {
            log.error("❌ Aktiviteler getirilirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktiviteler getirilemedi: " + e.getMessage()));
        }
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<ActivityLogDTO>>> getActivitiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "100") int limit) {

        log.info("📋 API: GET /api/v1/analytics/activities/range?startDate={}&endDate={}&limit={}",
                startDate, endDate, limit);

        try {
            List<ActivityLogDTO> activities = activityLogService
                    .getActivitiesByDateRange(startDate, endDate, limit);
            return ResponseEntity.ok(ApiResponse.success(activities));

        } catch (Exception e) {
            log.error("❌ Aktiviteler getirilirken hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktiviteler getirilemedi: " + e.getMessage()));
        }
    }

    /**
     * Aktivite tipi istatistikleri
     * GET /api/v1/analytics/activities/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getActivityTypeStats() {
        log.info("📊 API: GET /api/v1/analytics/activities/stats");

        try {
            Map<String, Long> stats = activityLogService.getActivityTypeStats();
            return ResponseEntity.ok(ApiResponse.success(stats));

        } catch (Exception e) {
            log.error("❌ İstatistikler getirilirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("İstatistikler getirilemedi: " + e.getMessage()));
        }
    }

    /**
     * Aktivite güncelle (✅ YENİ ENDPOINT)
     * PUT /api/v1/analytics/activities/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityLogDTO>> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityLogDTO activityDTO) {

        log.info("✏️ API: PUT /api/v1/analytics/activities/{}", id);
        log.info("✏️ Update Data: {}", activityDTO);

        try {
            // Customer validation
            if (activityDTO.getCustomerId() != null) {
                validateCustomer(activityDTO.getCustomerId());
            }

            // Update
            ActivityLogDTO updated = activityLogService.updateActivity(id, activityDTO);
            log.info("✅ Aktivite başarıyla güncellendi: ID={}", updated.getId());
            return ResponseEntity.ok(ApiResponse.success(updated));

        } catch (CustomerNotFoundException e) {
            log.error("❌ Müşteri bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (RuntimeException e) {
            log.error("❌ Aktivite bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Aktivite güncellenirken hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktivite güncellenemedi: " + e.getMessage()));
        }
    }

    /**
     * Aktivite sil
     * DELETE /api/v1/analytics/activities/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(@PathVariable Long id) {
        log.info("🗑️ API: DELETE /api/v1/analytics/activities/{}", id);

        try {
            activityLogService.deleteActivity(id);
            return ResponseEntity.ok(ApiResponse.success(null));

        } catch (RuntimeException e) {
            log.error("❌ Aktivite bulunamadı: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Aktivite silinirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Aktivite silinemedi: " + e.getMessage()));
        }
    }

    // ========== HELPER METHODS ==========

    /**
     * Müşteri varlığını kontrol et
     */
    private void validateCustomer(Long customerId) {
        try {
            ApiResponseDTO<CustomerResponseDTO> response = customerServiceClient.getCustomerById(customerId);

            if (!response.isSuccess() || response.getData() == null) {
                throw new CustomerNotFoundException("Müşteri bulunamadı: ID = " + customerId);
            }

        } catch (Exception e) {
            log.error("❌ Müşteri kontrolü başarısız: {}", e.getMessage());
            throw new CustomerNotFoundException("Müşteri bulunamadı: ID = " + customerId);
        }
    }

    /**
     * Custom Exception - Customer Not Found
     */
    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }
}