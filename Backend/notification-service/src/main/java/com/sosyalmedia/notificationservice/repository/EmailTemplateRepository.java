package com.sosyalmedia.notificationservice.repository;

import com.sosyalmedia.notificationservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    /**
     * Template key'e göre şablon bul
     */
    Optional<EmailTemplate> findByTemplateKey(String templateKey);

    /**
     * Template key'in var olup olmadığını kontrol et
     */
    boolean existsByTemplateKey(String templateKey);

    /**
     * Aktif şablonları getir
     */
    List<EmailTemplate> findByIsActiveTrue();

    /**
     * Tipine göre şablonları getir
     */
    List<EmailTemplate> findByTemplateType(String templateType);
}