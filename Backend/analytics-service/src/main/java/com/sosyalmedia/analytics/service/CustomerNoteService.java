// src/main/java/com/sosyalmedia/analytics/service/CustomerNoteService.java

package com.sosyalmedia.analytics.service;

import com.sosyalmedia.analytics.dto.CustomerNoteDTO;

import java.util.List;

public interface CustomerNoteService {

    /**
     * Yeni not ekle
     */
    CustomerNoteDTO createNote(CustomerNoteDTO noteDTO);

    /**
     * Not güncelle
     */
    CustomerNoteDTO updateNote(Long noteId, CustomerNoteDTO noteDTO);

    /**
     * Not sil
     */
    void deleteNote(Long noteId);

    /**
     * Not ID'ye göre getir
     */
    CustomerNoteDTO getNoteById(Long noteId);

    /**
     * Müşterinin tüm notlarını getir
     */
    List<CustomerNoteDTO> getNotesByCustomerId(Long customerId);

    /**
     * Müşteri not sayısını getir
     */
    long countNotesByCustomerId(Long customerId);
}