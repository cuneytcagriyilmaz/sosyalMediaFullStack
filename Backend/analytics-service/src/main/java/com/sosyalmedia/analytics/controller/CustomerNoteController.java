// src/main/java/com/sosyalmedia/analytics/controller/CustomerNoteController.java

package com.sosyalmedia.analytics.controller;

import com.sosyalmedia.analytics.dto.CustomerNoteDTO;
import com.sosyalmedia.analytics.dto.response.ApiResponse;
import com.sosyalmedia.analytics.service.CustomerNoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/notes")
@RequiredArgsConstructor
@Slf4j
public class CustomerNoteController {

    private final CustomerNoteService customerNoteService;

    /**
     * Tüm notları getir (admin için)
     * GET /api/v1/analytics/notes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerNoteDTO>> getNoteById(
            @PathVariable Long id) {

        log.info("GET /api/v1/analytics/notes/{}", id);

        CustomerNoteDTO note = customerNoteService.getNoteById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Not getirildi", note)
        );
    }

    /**
     * Müşteri notunu güncelle
     * PUT /api/v1/analytics/notes/{id}
     */
    @PutMapping("/{id}")

    public ResponseEntity<ApiResponse<CustomerNoteDTO>> updateNote(
            @PathVariable Long id,
            @RequestBody CustomerNoteDTO noteDTO) {

        log.info("PUT /api/v1/analytics/notes/{}", id);

        CustomerNoteDTO updated = customerNoteService.updateNote(id, noteDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Not güncellendi", updated)
        );
    }

    /**
     * Notu sil
     * DELETE /api/v1/analytics/notes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @PathVariable Long id) {

        log.info("DELETE /api/v1/analytics/notes/{}", id);

        customerNoteService.deleteNote(id);

        return ResponseEntity.ok(
                ApiResponse.success("Not silindi", null)
        );
    }

    /**
     * Müşteri not sayısını getir
     * GET /api/v1/analytics/notes/customer/{customerId}/count
     */
    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<ApiResponse<Long>> getNotesCount(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/analytics/notes/customer/{}/count", customerId);

        long count = customerNoteService.countNotesByCustomerId(customerId);

        return ResponseEntity.ok(
                ApiResponse.success("Not sayısı getirildi", count)
        );
    }
}