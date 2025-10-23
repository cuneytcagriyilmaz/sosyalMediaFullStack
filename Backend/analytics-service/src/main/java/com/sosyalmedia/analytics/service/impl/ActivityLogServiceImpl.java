// Backend/analytics-service/src/main/java/com/sosyalmedia/analytics/service/impl/ActivityLogServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.entity.ActivityLog;
import com.sosyalmedia.analytics.repository.ActivityLogRepository;
import com.sosyalmedia.analytics.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    @Transactional
    public ActivityLogDTO createActivity(ActivityLogDTO dto) {
        log.info("📝 Yeni aktivite oluşturuluyor: {}", dto.getMessage());

        // ✅ String'i Enum'a çevir
        ActivityLog.ActivityType activityType;
        try {
            activityType = ActivityLog.ActivityType.valueOf(dto.getActivityType().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("❌ Geçersiz aktivite tipi: {}", dto.getActivityType());
            throw new IllegalArgumentException("Geçersiz aktivite tipi: " + dto.getActivityType());
        }

        ActivityLog activity = ActivityLog.builder()
                .customerId(dto.getCustomerId())
                .activityType(activityType)
                .message(dto.getMessage())
                .icon(dto.getIcon())
                .createdAt(LocalDateTime.now())
                .build();

        ActivityLog saved = activityLogRepository.save(activity);

        log.info("✅ Aktivite oluşturuldu: ID={}, Type={}, Customer={}",
                saved.getId(), saved.getActivityType(), saved.getCustomerId());

        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getRecentActivities(int limit) {
        log.info("📋 Son {} aktivite getiriliyor", limit);

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ActivityLog> activities = activityLogRepository.findAll(pageRequest).getContent();

        log.info("✅ {} aktivite getirildi", activities.size());

        return activities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getCustomerActivities(Long customerId, int limit) {
        log.info("📋 Müşteri {} için {} aktivite getiriliyor", customerId, limit);

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ActivityLog> activities = activityLogRepository.findByCustomerId(customerId, pageRequest);

        log.info("✅ Müşteri {} için {} aktivite getirildi", customerId, activities.size());

        return activities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getActivitiesByType(String activityType, int limit) {
        log.info("📋 {} tipi aktiviteler getiriliyor", activityType);

        // ✅ String'i Enum'a çevir
        ActivityLog.ActivityType type;
        try {
            type = ActivityLog.ActivityType.valueOf(activityType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("❌ Geçersiz aktivite tipi: {}", activityType);
            throw new IllegalArgumentException("Geçersiz aktivite tipi: " + activityType);
        }

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ActivityLog> activities = activityLogRepository.findByActivityType(type, pageRequest);

        log.info("✅ {} tipi {} aktivite getirildi", activityType, activities.size());

        return activities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        log.info("📋 {} - {} tarihleri arası aktiviteler getiriliyor", startDate, endDate);

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ActivityLog> activities = activityLogRepository.findByDateRange(startDate, endDate, pageRequest);

        log.info("✅ {} aktivite getirildi", activities.size());

        return activities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getCustomerActivitiesByType(Long customerId, String activityType, int limit) {
        log.info("📋 Müşteri {} için {} tipi aktiviteler getiriliyor", customerId, activityType);

        // ✅ String'i Enum'a çevir
        ActivityLog.ActivityType type;
        try {
            type = ActivityLog.ActivityType.valueOf(activityType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("❌ Geçersiz aktivite tipi: {}", activityType);
            throw new IllegalArgumentException("Geçersiz aktivite tipi: " + activityType);
        }

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ActivityLog> activities = activityLogRepository.findByCustomerIdAndActivityType(customerId, type, pageRequest);

        log.info("✅ {} aktivite getirildi", activities.size());

        return activities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getActivityTypeStats() {
        log.info("📊 Aktivite tipi istatistikleri getiriliyor");

        List<Object[]> results = activityLogRepository.countByActivityType();

        Map<String, Long> stats = new HashMap<>();
        for (Object[] result : results) {
            ActivityLog.ActivityType type = (ActivityLog.ActivityType) result[0];
            Long count = (Long) result[1];
            // ✅ Enum'ı String'e çevir
            stats.put(type.name(), count);
        }

        log.info("✅ {} farklı aktivite tipi bulundu", stats.size());

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityLogDTO getActivityById(Long id) {
        log.info("📋 Aktivite {} getiriliyor", id);

        ActivityLog activity = activityLogRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Aktivite bulunamadı: ID={}", id);
                    return new RuntimeException("Activity not found with id: " + id);
                });

        log.info("✅ Aktivite bulundu: ID={}", id);

        return toDTO(activity);
    }

    @Override
    @Transactional
    public ActivityLogDTO updateActivity(Long id, ActivityLogDTO activityDTO) {
        log.info("✏️ Aktivite güncelleniyor: ID={}", id);

        ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));

        // Update fields
        if (activityDTO.getCustomerId() != null) {
            activityLog.setCustomerId(activityDTO.getCustomerId());
        }
        if (activityDTO.getActivityType() != null) {
            activityLog.setActivityType(ActivityLog.ActivityType.valueOf(activityDTO.getActivityType()));
        }
        if (activityDTO.getMessage() != null) {
            activityLog.setMessage(activityDTO.getMessage());
        }
        if (activityDTO.getIcon() != null) {
            activityLog.setIcon(activityDTO.getIcon());
        }

        ActivityLog updated = activityLogRepository.save(activityLog);
        log.info("✅ Aktivite güncellendi: ID={}", updated.getId());

        return toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        log.info("🗑️ Aktivite {} siliniyor", id);

        if (!activityLogRepository.existsById(id)) {
            log.error("❌ Aktivite bulunamadı: ID={}", id);
            throw new RuntimeException("Activity not found with id: " + id);
        }

        activityLogRepository.deleteById(id);

        log.info("✅ Aktivite {} silindi", id);
    }

    @Override
    @Transactional
    public List<ActivityLogDTO> createActivities(List<ActivityLogDTO> activities) {
        log.info("📝 {} adet aktivite toplu olarak oluşturuluyor", activities.size());

        List<ActivityLog> entities = activities.stream()
                .map(dto -> {
                    // ✅ String'i Enum'a çevir
                    ActivityLog.ActivityType activityType;
                    try {
                        activityType = ActivityLog.ActivityType.valueOf(dto.getActivityType().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        log.error("❌ Geçersiz aktivite tipi: {}", dto.getActivityType());
                        throw new IllegalArgumentException("Geçersiz aktivite tipi: " + dto.getActivityType());
                    }

                    return ActivityLog.builder()
                            .customerId(dto.getCustomerId())
                            .activityType(activityType)
                            .message(dto.getMessage())
                            .icon(dto.getIcon())
                            .createdAt(LocalDateTime.now())
                            .build();
                })
                .collect(Collectors.toList());

        List<ActivityLog> saved = activityLogRepository.saveAll(entities);

        log.info("✅ {} aktivite başarıyla oluşturuldu", saved.size());

        return saved.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Entity -> DTO dönüşümü
     * ✅ Enum'ı String'e çevir
     */
    private ActivityLogDTO toDTO(ActivityLog entity) {
        return ActivityLogDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .activityType(entity.getActivityType().name()) // ✅ Enum -> String
                .message(entity.getMessage())
                .icon(entity.getIcon())
                .timestamp(entity.getCreatedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}