package com.sosyalmedia.notificationservice.controller;

import com.sosyalmedia.notificationservice.dto.request.EmailTemplateRequestDTO;
import com.sosyalmedia.notificationservice.dto.response.EmailTemplateDTO;
import com.sosyalmedia.notificationservice.model.EmailTemplate;
import com.sosyalmedia.notificationservice.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/email-templates")
@RequiredArgsConstructor
@Slf4j
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    // ==================== CREATE ====================

    /**
     * Yeni e-posta şablonu oluştur
     * POST /api/email-templates
     */
    @PostMapping
    public ResponseEntity<EmailTemplateDTO> createTemplate(
            @Valid @RequestBody EmailTemplateRequestDTO request
    ) {
        log.info("📧 Yeni e-posta şablonu oluşturuluyor: {}", request.getTemplateName());
        EmailTemplate template = emailTemplateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(template));
    }

    // ==================== UPDATE ====================

    /**
     * E-posta şablonunu güncelle
     * PUT /api/email-templates/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailTemplateDTO> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody EmailTemplateRequestDTO request
    ) {
        log.info("📧 E-posta şablonu güncelleniyor - ID: {}", id);
        EmailTemplate template = emailTemplateService.updateTemplate(id, request);
        return ResponseEntity.ok(convertToDTO(template));
    }

    /**
     * Şablonu aktif/pasif yap
     * PUT /api/email-templates/{id}/toggle-active
     */
    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<EmailTemplateDTO> toggleTemplateActive(@PathVariable Long id) {
        log.info("🔄 Template aktiflik durumu değiştiriliyor - ID: {}", id);
        EmailTemplate template = emailTemplateService.toggleTemplateActive(id);
        return ResponseEntity.ok(convertToDTO(template));
    }

    // ==================== DELETE ====================

    /**
     * E-posta şablonunu sil
     * DELETE /api/email-templates/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        log.info("🗑️ E-posta şablonu siliniyor - ID: {}", id);
        emailTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== GET - SINGLE ====================

    /**
     * ID'ye göre şablon getir
     * GET /api/email-templates/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailTemplateDTO> getTemplateById(@PathVariable Long id) {
        log.info("🔍 E-posta şablonu getiriliyor - ID: {}", id);
        EmailTemplate template = emailTemplateService.getTemplateById(id);
        return ResponseEntity.ok(convertToDTO(template));
    }

    /**
     * Template key'e göre şablon getir
     * GET /api/email-templates/key/{templateKey}
     */
    @GetMapping("/key/{templateKey}")
    public ResponseEntity<EmailTemplateDTO> getTemplateByKey(@PathVariable String templateKey) {
        log.info("🔍 Template key'e göre getiriliyor: {}", templateKey);
        EmailTemplate template = emailTemplateService.getTemplateByKey(templateKey);
        return ResponseEntity.ok(convertToDTO(template));
    }

    // ==================== GET - LIST ====================

    /**
     * Tüm e-posta şablonlarını getir
     * GET /api/email-templates
     */
    @GetMapping
    public ResponseEntity<List<EmailTemplateDTO>> getAllTemplates() {
        log.info("📋 Tüm e-posta şablonları getiriliyor");
        List<EmailTemplate> templates = emailTemplateService.getAllTemplates();
        return ResponseEntity.ok(convertToDTOs(templates));
    }

    /**
     * Aktif e-posta şablonlarını getir
     * GET /api/email-templates/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<EmailTemplateDTO>> getActiveTemplates() {
        log.info("📋 Aktif e-posta şablonları getiriliyor");
        List<EmailTemplate> templates = emailTemplateService.getActiveTemplates();
        return ResponseEntity.ok(convertToDTOs(templates));
    }

    /**
     * Tipine göre şablonları getir
     * GET /api/email-templates/type/{templateType}
     */
    @GetMapping("/type/{templateType}")
    public ResponseEntity<List<EmailTemplateDTO>> getTemplatesByType(
            @PathVariable String templateType
    ) {
        log.info("📋 Tipine göre template'ler getiriliyor: {}", templateType);
        List<EmailTemplate> templates = emailTemplateService.getTemplatesByType(templateType);
        return ResponseEntity.ok(convertToDTOs(templates));
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * EmailTemplate -> EmailTemplateDTO
     */
    private EmailTemplateDTO convertToDTO(EmailTemplate template) {
        return EmailTemplateDTO.builder()
                .id(template.getId())
                .templateKey(template.getTemplateKey())
                .templateName(template.getTemplateName())
                .subjectTemplate(template.getSubjectTemplate())
                .bodyTemplate(template.getBodyTemplate())
                .templateType(template.getTemplateType())
                .variables(template.getVariables())
                .isActive(template.getIsActive())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }

    /**
     * List<EmailTemplate> -> List<EmailTemplateDTO>
     */
    private List<EmailTemplateDTO> convertToDTOs(List<EmailTemplate> templates) {
        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}