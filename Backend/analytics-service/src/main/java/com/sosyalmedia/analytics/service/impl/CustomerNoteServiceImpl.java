// src/main/java/com/sosyalmedia/analytics/service/impl/CustomerNoteServiceImpl.java

package com.sosyalmedia.analytics.service.impl;

import com.sosyalmedia.analytics.dto.CustomerNoteDTO;
import com.sosyalmedia.analytics.entity.CustomerNote;
import com.sosyalmedia.analytics.exception.ResourceNotFoundException;
import com.sosyalmedia.analytics.mapper.CustomerNoteMapper;
import com.sosyalmedia.analytics.repository.CustomerNoteRepository;
import com.sosyalmedia.analytics.service.CustomerNoteService;
import com.sosyalmedia.analytics.service.CustomerValidationService;
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
public class CustomerNoteServiceImpl implements CustomerNoteService {

    private final CustomerNoteRepository noteRepository;
    private final CustomerNoteMapper noteMapper;
    private final CustomerValidationService customerValidationService;

    @Override
    public CustomerNoteDTO createNote(CustomerNoteDTO noteDTO) {
        log.info("Creating new note for customer: {}", noteDTO.getCustomerId());

        // ✅ VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(noteDTO.getCustomerId());

        CustomerNote note = noteMapper.toEntity(noteDTO);
        note.setCreatedAt(LocalDateTime.now());

        // CreatedBy yoksa default değer
        if (note.getCreatedBy() == null || note.getCreatedBy().isEmpty()) {
            note.setCreatedBy("Admin");
        }

        CustomerNote savedNote = noteRepository.save(note);
        log.info("Customer note created successfully with ID: {}", savedNote.getId());

        return noteMapper.toDTO(savedNote);
    }

    @Override
    public CustomerNoteDTO updateNote(Long noteId, CustomerNoteDTO noteDTO) {
        log.info("Updating customer note with ID: {}", noteId);

        CustomerNote existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerNote", "id", noteId));

        existingNote.setText(noteDTO.getText());

        CustomerNote updatedNote = noteRepository.save(existingNote);
        log.info("Customer note updated successfully with ID: {}", updatedNote.getId());

        return noteMapper.toDTO(updatedNote);
    }

    @Override
    public void deleteNote(Long noteId) {
        log.info("Deleting customer note with ID: {}", noteId);

        CustomerNote note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerNote", "id", noteId));

        noteRepository.delete(note);
        log.info("Customer note deleted successfully with ID: {}", noteId);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerNoteDTO getNoteById(Long noteId) {
        log.info("Fetching customer note with ID: {}", noteId);

        CustomerNote note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerNote", "id", noteId));

        return noteMapper.toDTO(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerNoteDTO> getNotesByCustomerId(Long customerId) {
        log.info("Fetching all notes for customer: {}", customerId);

        //  VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        List<CustomerNote> notes = noteRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return noteMapper.toDTOList(notes);
    }

    @Override
    @Transactional(readOnly = true)
    public long countNotesByCustomerId(Long customerId) {
        //  VALIDATION: Müşteri var mı kontrol et
        customerValidationService.validateCustomerExists(customerId);

        return noteRepository.countByCustomerId(customerId);
    }
}