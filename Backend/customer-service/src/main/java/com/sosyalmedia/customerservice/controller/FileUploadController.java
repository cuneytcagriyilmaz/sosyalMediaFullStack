package com.sosyalmedia.customerservice.controller;

import com.sosyalmedia.customerservice.config.FileUploadProperties;
import com.sosyalmedia.customerservice.dto.response.ApiResponse;
import com.sosyalmedia.customerservice.dto.CustomerMediaDTO;
import com.sosyalmedia.customerservice.entity.Customer;
import com.sosyalmedia.customerservice.entity.CustomerMedia;
import com.sosyalmedia.customerservice.exception.CustomerNotFoundException;
import com.sosyalmedia.customerservice.mapper.CustomerMapper;
import com.sosyalmedia.customerservice.repository.CustomerMediaRepository;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import com.sosyalmedia.customerservice.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/customers/{customerId}/media")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "Media Management", description = "Müşteri Medya Yönetimi")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final CustomerRepository customerRepository;
    private final CustomerMediaRepository mediaRepository;
    private final FileUploadProperties fileUploadProperties;
    private final CustomerMapper customerMapper;

    // ========== TEK ENDPOINT İLE TÜM TİPLER ==========

    @PostMapping("/upload")
    @Transactional
    @Operation(summary = "Medya yükle", description = "Logo, fotoğraf, video veya doküman yükler")
    public ResponseEntity<ApiResponse<CustomerMediaDTO>> uploadMedia(
            @Parameter(description = "Müşteri ID", required = true)
            @PathVariable Long customerId,

            @Parameter(description = "Dosya", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Medya türü (LOGO, PHOTO, VIDEO, DOCUMENT)", required = true)
            @RequestParam("mediaType") CustomerMedia.MediaType mediaType) {

        log.info("Uploading media for customer ID: {}, type: {}, file: {}",
                customerId, mediaType, file.getOriginalFilename());

        // 1. Müşteriyi bul
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // 2. Dosyayı fiziksel olarak kaydet
        String fileType = mediaType.toString().toLowerCase() + "s"; // LOGO -> logos
        String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), fileType);

        // 3. DB'ye kaydet
        CustomerMedia media = CustomerMedia.builder()
                .filePath(filePath)
                .mediaType(mediaType)
                .originalFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .customer(customer)
                .build();

        CustomerMedia savedMedia = mediaRepository.save(media);

        log.info("Media uploaded successfully - ID: {}, Path: {}", savedMedia.getId(), filePath);

        // 4. DTO'ya çevir (fullUrl dahil)
        CustomerMediaDTO response = customerMapper.toMediaDTO(savedMedia);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medya başarıyla yüklendi", response));
    }

    // ========== ÇOKLU DOSYA YÜKLEME ==========

    @PostMapping("/upload-batch")
    @Transactional
    @Operation(summary = "Çoklu medya yükle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadMultiple(
            @PathVariable Long customerId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("mediaType") CustomerMedia.MediaType mediaType) {

        log.info("Batch upload - Customer ID: {}, Type: {}, Files count: {}",
                customerId, mediaType, files.size());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<CustomerMediaDTO> uploadedFiles = new ArrayList<>();
        String fileType = mediaType.toString().toLowerCase() + "s";

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Dosyayı kaydet
                String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), fileType);

                // DB'ye kaydet
                CustomerMedia media = CustomerMedia.builder()
                        .filePath(filePath)
                        .mediaType(mediaType)
                        .originalFileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .customer(customer)
                        .build();

                CustomerMedia savedMedia = mediaRepository.save(media);
                uploadedFiles.add(customerMapper.toMediaDTO(savedMedia));
            }
        }

        Map<String, Object> response = Map.of(
                "uploadedCount", uploadedFiles.size(),
                "files", uploadedFiles
        );

        log.info("Batch upload completed - {} files uploaded", uploadedFiles.size());

        return ResponseEntity.ok(ApiResponse.success(
                uploadedFiles.size() + " dosya yüklendi", response));
    }

    // ========== MEDYA LİSTELE ==========

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Müşteri medyalarını listele")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listMedia(
            @PathVariable Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<CustomerMedia> allMedia = mediaRepository.findByCustomerId(customerId);

        // Tipe göre grupla ve DTO'ya çevir
        Map<CustomerMedia.MediaType, List<CustomerMediaDTO>> groupedMedia = allMedia.stream()
                .collect(Collectors.groupingBy(
                        CustomerMedia::getMediaType,
                        Collectors.mapping(customerMapper::toMediaDTO, Collectors.toList())
                ));

        Map<String, Object> response = Map.of(
                "logos", groupedMedia.getOrDefault(CustomerMedia.MediaType.LOGO, List.of()),
                "photos", groupedMedia.getOrDefault(CustomerMedia.MediaType.PHOTO, List.of()),
                "videos", groupedMedia.getOrDefault(CustomerMedia.MediaType.VIDEO, List.of()),
                "documents", groupedMedia.getOrDefault(CustomerMedia.MediaType.DOCUMENT, List.of()),
                "totalFiles", allMedia.size()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ========== MEDYA SİL ==========

    @DeleteMapping("/{mediaId}")
    @Transactional
    @Operation(summary = "Medya sil")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(
            @PathVariable Long customerId,
            @PathVariable Long mediaId) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media bulunamadı: " + mediaId));

        if (!media.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Bu medya bu müşteriye ait değil");
        }

        // 1. Fiziksel dosyayı sil
        fileStorageService.deleteFile(media.getFilePath());

        // 2. DB'den sil
        mediaRepository.delete(media);

        log.info("Media deleted - ID: {}, Path: {}", mediaId, media.getFilePath());

        return ResponseEntity.ok(ApiResponse.success("Medya silindi", null));
    }

    // ========== DOSYA İNDİR ==========

    @GetMapping("/download/{mediaId}")
    @Operation(summary = "Medya indir")
    public ResponseEntity<Resource> downloadMedia(
            @PathVariable Long customerId,
            @PathVariable Long mediaId) {

        return serveFile(customerId, mediaId, true);
    }

    // ========== DOSYA GÖRÜNTÜLE ==========

    @GetMapping("/view/{mediaId}")
    @Operation(summary = "Medyayı tarayıcıda görüntüle")
    public ResponseEntity<Resource> viewMedia(
            @PathVariable Long customerId,
            @PathVariable Long mediaId) {

        return serveFile(customerId, mediaId, false);
    }

    // ========== HELPER METHOD ==========

    private ResponseEntity<Resource> serveFile(Long customerId, Long mediaId, boolean forceDownload) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media bulunamadı: " + mediaId));

        if (!media.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Bu medya bu müşteriye ait değil");
        }

        try {
            Path filePath = Paths.get(fileUploadProperties.getDirectory())
                    .resolve(media.getFilePath());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Dosya okunamıyor: " + media.getFilePath());
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            String disposition = forceDownload ? "attachment" : "inline";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            disposition + "; filename=\"" + media.getOriginalFileName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Dosya işlenemedi: " + e.getMessage());
        }
    }



    // ========== ÇOKLU MEDYA ZIP İNDİRME ==========

    @PostMapping("/download-zip")
    @Operation(summary = "Seçili medyaları ZIP olarak indir")
    public ResponseEntity<Resource> downloadMediaAsZip(
            @PathVariable Long customerId,
            @RequestBody List<Long> mediaIds) {

        log.info("Downloading ZIP for customer: {}, media count: {}", customerId, mediaIds.size());

        // 1. Müşteri kontrolü
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // 2. Seçili medyaları bul
        List<CustomerMedia> mediaList = mediaRepository.findAllById(mediaIds);

        if (mediaList.isEmpty()) {
            throw new RuntimeException("Seçili medya bulunamadı");
        }

        // 3. Geçici ZIP dosyası oluştur
        try {
            Path tempZip = Files.createTempFile(
                    "customer-" + customerId + "-media-",
                    ".zip"
            );

            // 4. ZIP'e dosyaları ekle
            try (ZipOutputStream zipOut =
                         new ZipOutputStream(Files.newOutputStream(tempZip))) {

                for (CustomerMedia media : mediaList) {
                    Path filePath = Paths.get(fileUploadProperties.getDirectory())
                            .resolve(media.getFilePath());

                    if (Files.exists(filePath)) {
                        // ZIP entry oluştur (orijinal dosya adıyla)
                        ZipEntry zipEntry =
                                new ZipEntry(media.getOriginalFileName());
                        zipOut.putNextEntry(zipEntry);

                        // Dosyayı ZIP'e kopyala
                        Files.copy(filePath, zipOut);
                        zipOut.closeEntry();

                        log.debug("Added to ZIP: {}", media.getOriginalFileName());
                    } else {
                        log.warn("File not found: {}", filePath);
                    }
                }
            }

            // 5. ZIP'i Resource olarak döndür
            Resource resource = new UrlResource(tempZip.toUri());

            String zipFileName = customer.getCompanyName()
                    .replaceAll("[^a-zA-Z0-9]", "-")
                    .toLowerCase() + "-medya.zip";

            log.info("ZIP created successfully: {} ({} files)",
                    zipFileName, mediaList.size());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + zipFileName + "\"")
                    .body(resource);

        } catch (IOException e) {
            log.error("ZIP creation failed: {}", e.getMessage());
            throw new RuntimeException("ZIP oluşturulamadı: " + e.getMessage());
        }
    }
}