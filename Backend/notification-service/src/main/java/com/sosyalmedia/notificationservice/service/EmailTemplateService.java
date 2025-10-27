package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.EmailTemplateRequestDTO;
import com.sosyalmedia.notificationservice.model.EmailTemplate;

import java.util.List;
import java.util.Map;

public interface EmailTemplateService {

    /**
     * Yeni template oluştur
     */
    EmailTemplate createTemplate(EmailTemplateRequestDTO requestDTO);

    /**
     * Template güncelle
     */
    EmailTemplate updateTemplate(Long id, EmailTemplateRequestDTO requestDTO);

    /**
     * Template sil
     */
    void deleteTemplate(Long id);

    /**
     * Template'i aktif/pasif yap
     */
    EmailTemplate toggleTemplateActive(Long id);

    /**
     * ID'ye göre template getir
     */
    EmailTemplate getTemplateById(Long id);

    /**
     * Template key'e göre getir
     */
    EmailTemplate getTemplateByKey(String templateKey);

    /**
     * Tüm template'leri getir
     */
    List<EmailTemplate> getAllTemplates();

    /**
     * Aktif template'leri getir
     */
    List<EmailTemplate> getActiveTemplates();

    /**
     * Tipine göre template'leri getir
     */
    List<EmailTemplate> getTemplatesByType(String templateType);

    // ==================== TEMPLATE PROCESSING ====================

    /**
     * Subject template'ini işle ve değişkenleri yerine koy
     */
    String processSubjectTemplate(String subjectTemplate, Map<String, String> variables);

    /**
     * Body template'ini işle ve değişkenleri yerine koy
     */
    String processBodyTemplate(String bodyTemplate, Map<String, String> variables);
}