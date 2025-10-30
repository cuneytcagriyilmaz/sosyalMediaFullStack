package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineArchiveResponse;
import com.sosyalmedia.notificationservice.entity.PostDeadline;
import com.sosyalmedia.notificationservice.entity.PostDeadlineArchive;
import com.sosyalmedia.notificationservice.exception.PostDeadlineArchiveNotFoundException;
import com.sosyalmedia.notificationservice.exception.PostDeadlineNotFoundException;
import com.sosyalmedia.notificationservice.mapper.PostDeadlineArchiveMapper;
import com.sosyalmedia.notificationservice.repository.PostDeadlineArchiveRepository;
import com.sosyalmedia.notificationservice.repository.PostDeadlineRepository;
import com.sosyalmedia.notificationservice.service.CustomerValidationService;
import com.sosyalmedia.notificationservice.service.PostDeadlineArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeadlineArchiveServiceImpl implements PostDeadlineArchiveService {

    private final PostDeadlineArchiveRepository archiveRepository;
    private final PostDeadlineRepository deadlineRepository;
    private final PostDeadlineArchiveMapper archiveMapper;
    private final CustomerValidationService customerValidationService;


    @Override
    @Transactional
    public PostDeadlineArchiveResponse archiveDeadline(Long deadlineId, String reason) {
        log.info("📦 Archiving deadline: ID={}, reason={}", deadlineId, reason);

        PostDeadline deadline = deadlineRepository.findById(deadlineId)
                .orElseThrow(() -> new PostDeadlineNotFoundException(deadlineId));

        PostDeadlineArchive archive = archiveMapper.toArchive(deadline, reason);
        PostDeadlineArchive saved = archiveRepository.save(archive);

        deadlineRepository.delete(deadline);

        log.info("✅ Deadline archived successfully: ID={}", saved.getId());

        CustomerBasicDTO customer = getCustomerSafely(saved.getCustomerId());
        return archiveMapper.toResponse(saved, customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDeadlineArchiveResponse> getAllArchivedDeadlines() {
        log.info("📋 Getting all archived deadlines");

        List<PostDeadlineArchive> archives = archiveRepository.findAllByOrderByArchivedAtDesc();

        return archives.stream()
                .map(this::toResponseWithCustomer)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDeadlineArchiveResponse> getArchivedDeadlinesByCustomer(Long customerId) {
        log.info("👤 Getting archived deadlines for customer: {}", customerId);

        List<PostDeadlineArchive> archives = archiveRepository.findByCustomerIdOrderByArchivedAtDesc(customerId);

        return archives.stream()
                .map(this::toResponseWithCustomer)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void restoreDeadline(Long archiveId) {
        log.info("♻️ Restoring deadline from archive: ID={}", archiveId);

        PostDeadlineArchive archive = archiveRepository.findById(archiveId)
                .orElseThrow(() -> new PostDeadlineArchiveNotFoundException(archiveId));

        PostDeadline restored = archiveMapper.toDeadline(archive);
        deadlineRepository.save(restored);

        archiveRepository.delete(archive);

        log.info("✅ Deadline restored successfully: ID={}", restored.getId());
    }

    @Override
    @Transactional
    public void deleteArchive(Long archiveId) {
        log.info("🗑️ Deleting archive: ID={}", archiveId);

        if (!archiveRepository.existsById(archiveId)) {
            throw new PostDeadlineArchiveNotFoundException(archiveId);
        }

        archiveRepository.deleteById(archiveId);
        log.info("✅ Archive deleted: ID={}", archiveId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalArchivedCount() {
        return archiveRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getArchivedCountLast30Days() {
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        return archiveRepository.countArchivedSince(since);
    }

    // ========== HELPER METHODS ==========

    private CustomerBasicDTO getCustomerSafely(Long customerId) {
        // ✅ DÜZELTME: CustomerValidationService kullan
        CustomerBasicDTO customer = customerValidationService.getCustomerSafely(customerId);

        // Customer null ise fallback
        if (customer == null) {
            log.warn("⚠️ Customer not found in archive: {}", customerId);
            return CustomerBasicDTO.builder()
                    .id(customerId)
                    .companyName("⚠️ Müşteri Bulunamadı (ID: " + customerId + ")")
                    .sector("Bilinmiyor")
                    .status("UNKNOWN")
                    .build();
        }

        return customer;
    }

    private PostDeadlineArchiveResponse toResponseWithCustomer(PostDeadlineArchive archive) {
        CustomerBasicDTO customer = getCustomerSafely(archive.getCustomerId());
        return archiveMapper.toResponse(archive, customer);
    }
}