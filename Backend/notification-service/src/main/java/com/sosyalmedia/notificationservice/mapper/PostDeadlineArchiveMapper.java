package com.sosyalmedia.notificationservice.mapper;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineArchiveResponse;
import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.PostDeadlineArchive;
import org.springframework.stereotype.Component;

@Component
public class PostDeadlineArchiveMapper {

    /**
     * PostDeadline -> PostDeadlineArchive (Arşivleme için)
     */
    public PostDeadlineArchive toArchive(PostDeadline deadline, String reason) {
        if (deadline == null) {
            return null;
        }

        return PostDeadlineArchive.builder()
                .originalDeadlineId(deadline.getId())
                .customerId(deadline.getCustomerId())
                .scheduledDate(deadline.getScheduledDate())
                .finalStatus(deadline.getStatus())
                .contentReady(deadline.getContentReady())
                .postContent(deadline.getPostContent())
                .platform(deadline.getPlatform()) // ✅ Platform enum
                .archivedReason(reason)
                .originalCreatedAt(deadline.getCreatedAt())
                .originalUpdatedAt(deadline.getUpdatedAt())
                .build();
    }

    /**
     * PostDeadlineArchive -> PostDeadlineArchiveResponse
     */
    public PostDeadlineArchiveResponse toResponse(PostDeadlineArchive archive) {
        if (archive == null) {
            return null;
        }

        return PostDeadlineArchiveResponse.builder()
                .id(archive.getId())
                .originalDeadlineId(archive.getOriginalDeadlineId())
                .customerId(archive.getCustomerId())
                .scheduledDate(archive.getScheduledDate())
                .finalStatus(archive.getFinalStatus())
                .statusDisplayName(archive.getFinalStatus().getDisplayName())
                .statusColorCode(archive.getFinalStatus().getColorCode())
                .contentReady(archive.getContentReady())
                .postContent(archive.getPostContent())
                .platform(archive.getPlatform()) // ✅ Platform enum
                // ✅ YENİ: Platform display bilgileri
                .platformDisplayName(archive.getPlatform() != null ? archive.getPlatform().getDisplayName() : null)
                .platformBrandColor(archive.getPlatform() != null ? archive.getPlatform().getBrandColor() : null)
                .archivedReason(archive.getArchivedReason())
                .archivedAt(archive.getArchivedAt())
                .originalCreatedAt(archive.getOriginalCreatedAt())
                .originalUpdatedAt(archive.getOriginalUpdatedAt())
                .build();
    }

    /**
     * PostDeadlineArchive + Customer -> PostDeadlineArchiveResponse (Tam bilgi)
     */
    public PostDeadlineArchiveResponse toResponse(PostDeadlineArchive archive, CustomerBasicDTO customer) {
        if (archive == null) {
            return null;
        }

        PostDeadlineArchiveResponse response = toResponse(archive);

        if (customer != null) {
            response.setCompanyName(customer.getCompanyName());
            response.setSector(customer.getSector());
        }

        return response;
    }

    /**
     * PostDeadlineArchive -> PostDeadline (Geri yükleme için)
     */
    public PostDeadline toDeadline(PostDeadlineArchive archive) {
        if (archive == null) {
            return null;
        }

        return PostDeadline.builder()
                .customerId(archive.getCustomerId())
                .scheduledDate(archive.getScheduledDate())
                .status(archive.getFinalStatus())
                .contentReady(archive.getContentReady())
                .postContent(archive.getPostContent())
                .platform(archive.getPlatform()) //  Platform enum
                .build();
    }
}