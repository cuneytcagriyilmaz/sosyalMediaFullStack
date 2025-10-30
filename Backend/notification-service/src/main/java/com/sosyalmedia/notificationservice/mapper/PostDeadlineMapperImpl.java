package com.sosyalmedia.notificationservice.mapper;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.request.PostDeadlineCreateRequest;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineResponse;
import com.sosyalmedia.notificationservice.entity.PostDeadline;
import org.springframework.stereotype.Component;

@Component
public class PostDeadlineMapperImpl implements PostDeadlineMapper {

    @Override
    public PostDeadline toEntity(PostDeadlineCreateRequest request) {
        if (request == null) {
            return null;
        }

        return PostDeadline.builder()
                .customerId(request.getCustomerId())
                .scheduledDate(request.getScheduledDate())
                .status(request.getStatus() != null ? request.getStatus() : PostDeadline.PostDeadlineStatus.NOT_STARTED)
                .contentReady(request.getContentReady() != null ? request.getContentReady() : false)
                .postContent(request.getPostContent())
                .platform(request.getPlatform())
                // ✅ YENİ: Manuel oluşturulanlarda default değerler
                .eventType(PostDeadline.EventType.REGULAR)
                .autoCreated(false)
                .build();
    }

    @Override
    public PostDeadlineResponse toResponse(PostDeadline entity) {
        if (entity == null) {
            return null;
        }

        return PostDeadlineResponse.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .scheduledDate(entity.getScheduledDate())
                .daysRemaining(entity.getDaysRemaining())
                .urgencyLevel(entity.getUrgencyLevel())
                .urgencyColor(entity.getUrgencyLevel().getColorCode())
                .urgencyDisplayName(entity.getUrgencyLevel().getDisplayName())
                .status(entity.getStatus())
                .statusDisplayName(entity.getStatus().getDisplayName())
                .statusColorCode(entity.getStatus().getColorCode())
                .contentReady(entity.getContentReady())
                .postContent(entity.getPostContent())
                .platform(entity.getPlatform())
                .platformDisplayName(entity.getPlatform() != null ? entity.getPlatform().getDisplayName() : null)
                .platformBrandColor(entity.getPlatform() != null ? entity.getPlatform().getBrandColor() : null)
                //  EventType bilgileri
                .eventType(entity.getEventType())
                .eventTypeDisplayName(entity.getEventType() != null ? entity.getEventType().getDisplayName() : null)
                .eventTypeColorCode(entity.getEventType() != null ? entity.getEventType().getColorCode() : null)
                .holidayName(entity.getHolidayName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public PostDeadlineResponse toResponse(PostDeadline entity, CustomerBasicDTO customer) {
        if (entity == null) {
            return null;
        }

        PostDeadlineResponse response = toResponse(entity);

        if (customer != null) {
            response.setCompanyName(customer.getCompanyName());
            response.setSector(customer.getSector());
            response.setCustomerStatus(customer.getStatus());
        }

        return response;
    }
}