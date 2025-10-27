package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.dto.request.EmailTemplateRequestDTO;
import com.sosyalmedia.notificationservice.exception.ResourceNotFoundException;
import com.sosyalmedia.notificationservice.model.EmailTemplate;
import com.sosyalmedia.notificationservice.repository.EmailTemplateRepository;
import com.sosyalmedia.notificationservice.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository templateRepository;

    @Override
    @Transactional
    public EmailTemplate createTemplate(EmailTemplateRequestDTO requestDTO) {
        log.info("📧 Yeni e-posta şablonu oluşturuluyor: {}", requestDTO.getTemplateName());

        // Template key'in benzersiz olup olmadığını kontrol et
        if (templateRepository.existsByTemplateKey(requestDTO.getTemplateKey())) {
            throw new IllegalArgumentException("Bu template key zaten kullanılıyor: " + requestDTO.getTemplateKey());
        }

        EmailTemplate template = EmailTemplate.builder()
                .templateKey(requestDTO.getTemplateKey())
                .templateName(requestDTO.getTemplateName())
                .subjectTemplate(requestDTO.getSubjectTemplate())
                .bodyTemplate(requestDTO.getBodyTemplate())
                .templateType(requestDTO.getTemplateType())
                .variables(requestDTO.getVariables())
                .isActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : true)
                .build();

        EmailTemplate saved = templateRepository.save(template);
        log.info("✅ E-posta şablonu oluşturuldu - ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public EmailTemplate updateTemplate(Long id, EmailTemplateRequestDTO requestDTO) {
        log.info("📧 E-posta şablonu güncelleniyor - ID: {}", id);

        EmailTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "id", id));

        // Template key değişiyorsa, yeni key'in benzersiz olup olmadığını kontrol et
        if (!template.getTemplateKey().equals(requestDTO.getTemplateKey())) {
            if (templateRepository.existsByTemplateKey(requestDTO.getTemplateKey())) {
                throw new IllegalArgumentException("Bu template key zaten kullanılıyor: " + requestDTO.getTemplateKey());
            }
        }

        template.setTemplateKey(requestDTO.getTemplateKey());
        template.setTemplateName(requestDTO.getTemplateName());
        template.setSubjectTemplate(requestDTO.getSubjectTemplate());
        template.setBodyTemplate(requestDTO.getBodyTemplate());
        template.setTemplateType(requestDTO.getTemplateType());
        template.setVariables(requestDTO.getVariables());

        if (requestDTO.getIsActive() != null) {
            template.setIsActive(requestDTO.getIsActive());
        }

        EmailTemplate updated = templateRepository.save(template);
        log.info("✅ E-posta şablonu güncellendi - ID: {}", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        log.info("🗑️ E-posta şablonu siliniyor - ID: {}", id);

        if (!templateRepository.existsById(id)) {
            throw new ResourceNotFoundException("EmailTemplate", "id", id);
        }

        templateRepository.deleteById(id);
        log.info("✅ E-posta şablonu silindi - ID: {}", id);
    }

    @Override
    @Transactional
    public EmailTemplate toggleTemplateActive(Long id) {
        log.info("🔄 Template aktiflik durumu değiştiriliyor - ID: {}", id);

        EmailTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "id", id));

        template.setIsActive(!template.getIsActive());

        EmailTemplate updated = templateRepository.save(template);
        log.info("✅ Template aktiflik durumu güncellendi - ID: {}, Durum: {}",
                id, updated.getIsActive() ? "Aktif" : "Pasif");

        return updated;
    }

    @Override
    public EmailTemplate getTemplateById(Long id) {
        log.debug("🔍 Template getiriliyor - ID: {}", id);
        return templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "id", id));
    }

    @Override
    public EmailTemplate getTemplateByKey(String templateKey) {
        log.debug("🔍 Template getiriliyor - Key: {}", templateKey);
        return templateRepository.findByTemplateKey(templateKey)
                .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "templateKey", templateKey));
    }

    @Override
    public List<EmailTemplate> getAllTemplates() {
        log.debug("📋 Tüm template'ler getiriliyor");
        return templateRepository.findAll();
    }

    @Override
    public List<EmailTemplate> getActiveTemplates() {
        log.debug("📋 Aktif template'ler getiriliyor");
        return templateRepository.findByIsActiveTrue();
    }

    @Override
    public List<EmailTemplate> getTemplatesByType(String templateType) {
        log.debug("📋 Tipine göre template'ler getiriliyor: {}", templateType);
        return templateRepository.findByTemplateType(templateType);
    }

    // ==================== TEMPLATE PROCESSING ====================

    @Override
    public String processSubjectTemplate(String subjectTemplate, Map<String, String> variables) {
        if (subjectTemplate == null || subjectTemplate.isEmpty()) {
            return "";
        }

        if (variables == null || variables.isEmpty()) {
            return subjectTemplate;
        }

        String processed = subjectTemplate;

        // Değişkenleri yerine koy
        // Format: {{variableName}}
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            processed = processed.replace(placeholder, value);
        }

        log.debug("📧 Subject template işlendi: {} -> {}", subjectTemplate, processed);
        return processed;
    }

    @Override
    public String processBodyTemplate(String bodyTemplate, Map<String, String> variables) {
        if (bodyTemplate == null || bodyTemplate.isEmpty()) {
            return "";
        }

        if (variables == null || variables.isEmpty()) {
            return bodyTemplate;
        }

        String processed = bodyTemplate;

        // Değişkenleri yerine koy
        // Format: {{variableName}}
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            processed = processed.replace(placeholder, value);
        }

        log.debug("📧 Body template işlendi");
        return processed;
    }
}