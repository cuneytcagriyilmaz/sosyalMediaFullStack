// src/main/java/com/sosyalmedia/analytics/service/impl/ActivityLogServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.dto.ActivityLogDTO;
import com.sosyalmedia.analytics.entity.ActivityLog;
import com.sosyalmedia.analytics.exception.ResourceNotFoundException;
import com.sosyalmedia.analytics.mapper.ActivityLogMapper;
import com.sosyalmedia.analytics.repository.ActivityLogRepository;
import com.sosyalmedia.analytics.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ActivityLogMapper activityLogMapper;

    @Override
    public ActivityLogDTO createActivityLog(ActivityLogDTO activityLogDTO) {
        log.info("Creating new activity log: {}", activityLogDTO.getMessage());

        ActivityLog activityLog = activityLogMapper.toEntity(activityLogDTO);
        activityLog.setCreatedAt(LocalDateTime.now());

        // Icon'u activity type'a göre set et
        if (activityLog.getIcon() == null || activityLog.getIcon().isEmpty()) {
            activityLog.setIcon(getIconForActivityType(activityLog.getActivityType()));
        }

        ActivityLog savedLog = activityLogRepository.save(activityLog);
        log.info("Activity log created successfully with ID: {}", savedLog.getId());

        return activityLogMapper.toDTO(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityLogDTO getActivityLogById(Long id) {
        log.info("Fetching activity log with ID: {}", id);

        ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityLog", "id", id));

        return activityLogMapper.toDTO(activityLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getRecentActivities(int limit) {
        log.info("Fetching {} recent activities", limit);

        List<ActivityLog> activities = activityLogRepository.findRecentActivities(limit);
        return activityLogMapper.toDTOList(activities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getRecentActivitiesByCustomerId(Long customerId, int limit) {
        log.info("Fetching {} recent activities for customer: {}", limit, customerId);

        List<ActivityLog> activities = activityLogRepository.findRecentActivitiesByCustomerId(customerId, limit);
        return activityLogMapper.toDTOList(activities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getActivitiesByType(ActivityLog.ActivityType activityType) {
        log.info("Fetching activities by type: {}", activityType);

        List<ActivityLog> activities = activityLogRepository.findByActivityTypeOrderByCreatedAtDesc(activityType);
        return activityLogMapper.toDTOList(activities);
    }

    @Override
    public void deleteActivityLog(Long id) {
        log.info("Deleting activity log with ID: {}", id);

        ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityLog", "id", id));

        activityLogRepository.delete(activityLog);
        log.info("Activity log deleted successfully with ID: {}", id);
    }

    /**
     * Activity type'a göre icon belirle
     */
    private String getIconForActivityType(ActivityLog.ActivityType type) {
        return switch (type) {
            case POST_SENT -> "📤";
            case POST_READY -> "✅";
            case AI_COMPLETED -> "🤖";
            case DEADLINE_APPROACHING -> "⏰";
            case NEW_CUSTOMER -> "👤";
            case CUSTOMER_UPDATED -> "✏️";
            case CONTENT_APPROVED -> "👍";
            case MEDIA_UPLOADED -> "📸";
        };
    }
}