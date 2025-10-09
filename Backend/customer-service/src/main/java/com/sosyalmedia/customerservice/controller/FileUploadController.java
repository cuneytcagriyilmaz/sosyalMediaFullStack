package com.sosyalmedia.customerservice.controller;

import com.sosyalmedia.customerservice.config.FileUploadProperties;
import com.sosyalmedia.customerservice.dto.ApiResponse;
import com.sosyalmedia.customerservice.entity.Customer;
import com.sosyalmedia.customerservice.entity.CustomerMedia;
import com.sosyalmedia.customerservice.exception.CustomerNotFoundException;
import com.sosyalmedia.customerservice.repository.CustomerMediaRepository;
import com.sosyalmedia.customerservice.repository.CustomerRepository;
import com.sosyalmedia.customerservice.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers/{customerId}/files")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "File Management", description = "Dosya yükleme ve yönetim")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final CustomerRepository customerRepository;
    private final CustomerMediaRepository mediaRepository;
    private final FileUploadProperties fileUploadProperties;

    @PostMapping("/logos")
    @Transactional
    @Operation(summary = "Logo yükle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadLogo(
            @PathVariable Long customerId,
            @RequestParam("file") MultipartFile file) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), "logos");

        CustomerMedia media = CustomerMedia.builder()
                .filePath(filePath)
                .mediaType(CustomerMedia.MediaType.LOGO)
                .originalFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .customer(customer)
                .build();

        CustomerMedia savedMedia = mediaRepository.save(media);

        Map<String, Object> response = new HashMap<>();
        response.put("mediaId", savedMedia.getId());
        response.put("filePath", filePath);
        response.put("fileName", file.getOriginalFilename());
        response.put("size", file.getSize());

        log.info("Logo uploaded - Customer: {}, Media ID: {}", customer.getCompanyName(), savedMedia.getId());

        return ResponseEntity.ok(ApiResponse.success("Logo yüklendi", response));
    }

    @PostMapping("/photos")
    @Transactional
    @Operation(summary = "Fotoğraf yükle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadPhoto(
            @PathVariable Long customerId,
            @RequestParam("file") MultipartFile file) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), "photos");

        CustomerMedia media = CustomerMedia.builder()
                .filePath(filePath)
                .mediaType(CustomerMedia.MediaType.PHOTO)
                .originalFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .customer(customer)
                .build();

        CustomerMedia savedMedia = mediaRepository.save(media);

        Map<String, Object> response = new HashMap<>();
        response.put("mediaId", savedMedia.getId());
        response.put("filePath", filePath);
        response.put("fileName", file.getOriginalFilename());
        response.put("size", file.getSize());

        return ResponseEntity.ok(ApiResponse.success("Fotoğraf yüklendi", response));
    }

    @PostMapping("/videos")
    @Transactional
    @Operation(summary = "Video yükle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadVideo(
            @PathVariable Long customerId,
            @RequestParam("file") MultipartFile file) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), "videos");

        CustomerMedia media = CustomerMedia.builder()
                .filePath(filePath)
                .mediaType(CustomerMedia.MediaType.VIDEO)
                .originalFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .customer(customer)
                .build();

        CustomerMedia savedMedia = mediaRepository.save(media);

        Map<String, Object> response = new HashMap<>();
        response.put("mediaId", savedMedia.getId());
        response.put("filePath", filePath);
        response.put("fileName", file.getOriginalFilename());
        response.put("size", file.getSize());

        return ResponseEntity.ok(ApiResponse.success("Video yüklendi", response));
    }

    @PostMapping("/documents")
    @Transactional
    @Operation(summary = "Doküman yükle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadDocument(
            @PathVariable Long customerId,
            @RequestParam("file") MultipartFile file) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), "documents");

        CustomerMedia media = CustomerMedia.builder()
                .filePath(filePath)
                .mediaType(CustomerMedia.MediaType.DOCUMENT)
                .originalFileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .customer(customer)
                .build();

        CustomerMedia savedMedia = mediaRepository.save(media);

        Map<String, Object> response = new HashMap<>();
        response.put("mediaId", savedMedia.getId());
        response.put("filePath", filePath);
        response.put("fileName", file.getOriginalFilename());
        response.put("size", file.getSize());

        return ResponseEntity.ok(ApiResponse.success("Doküman yüklendi", response));
    }

    @PostMapping("/batch")
    @Transactional
    @Operation(summary = "Çoklu dosya yükle")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadMultiple(
            @PathVariable Long customerId,
            @RequestParam("type") String type,
            @RequestParam("files") List<MultipartFile> files) {

        // DEBUG LOGLARI
        log.info("=== BATCH UPLOAD DEBUG ===");
        log.info("Customer ID: {}", customerId);
        log.info("Type: {}", type);
        log.info("Files count received: {}", files.size());
        for (int i = 0; i < files.size(); i++) {
            log.info("File {}: {} ({} bytes)", i + 1, files.get(i).getOriginalFilename(), files.get(i).getSize());
        }
        log.info("==========================");

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // type validation
        if (!List.of("logos", "photos", "videos", "documents").contains(type)) {
            throw new IllegalArgumentException("Geçersiz tip: " + type);
        }

        CustomerMedia.MediaType mediaType = switch (type) {
            case "logos" -> CustomerMedia.MediaType.LOGO;
            case "photos" -> CustomerMedia.MediaType.PHOTO;
            case "videos" -> CustomerMedia.MediaType.VIDEO;
            default -> CustomerMedia.MediaType.DOCUMENT;
        };

        List<Map<String, Object>> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filePath = fileStorageService.storeFile(file, customer.getCompanyName(), type);

                CustomerMedia media = CustomerMedia.builder()
                        .filePath(filePath)
                        .mediaType(mediaType)
                        .originalFileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .customer(customer)
                        .build();

                CustomerMedia savedMedia = mediaRepository.save(media);

                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("mediaId", savedMedia.getId());
                fileInfo.put("fileName", file.getOriginalFilename());
                fileInfo.put("filePath", filePath);
                fileInfo.put("size", file.getSize());
                uploadedFiles.add(fileInfo);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("uploadedCount", uploadedFiles.size());
        response.put("files", uploadedFiles);

        return ResponseEntity.ok(ApiResponse.success(uploadedFiles.size() + " dosya yüklendi", response));
    }

    @DeleteMapping("/{mediaId}")
    @Transactional
    @Operation(summary = "Dosya sil")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable Long customerId,
            @PathVariable Long mediaId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media bulunamadı: " + mediaId));

        if (!media.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Bu dosya bu müşteriye ait değil");
        }

        fileStorageService.deleteFile(media.getFilePath());
        mediaRepository.delete(media);

        return ResponseEntity.ok(ApiResponse.success("Dosya silindi", null));
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Dosyaları listele")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listFiles(
            @PathVariable Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<CustomerMedia> allMedia = mediaRepository.findByCustomerId(customerId);

        List<Map<String, Object>> logos = allMedia.stream()
                .filter(m -> m.getMediaType() == CustomerMedia.MediaType.LOGO)
                .map(this::mapMedia)
                .toList();

        List<Map<String, Object>> photos = allMedia.stream()
                .filter(m -> m.getMediaType() == CustomerMedia.MediaType.PHOTO)
                .map(this::mapMedia)
                .toList();

        List<Map<String, Object>> videos = allMedia.stream()
                .filter(m -> m.getMediaType() == CustomerMedia.MediaType.VIDEO)
                .map(this::mapMedia)
                .toList();

        List<Map<String, Object>> documents = allMedia.stream()
                .filter(m -> m.getMediaType() == CustomerMedia.MediaType.DOCUMENT)
                .map(this::mapMedia)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("logos", logos);
        response.put("photos", photos);
        response.put("videos", videos);
        response.put("documents", documents);
        response.put("totalFiles", allMedia.size());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Map<String, Object> mapMedia(CustomerMedia m) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", m.getId());
        map.put("filePath", m.getFilePath());
        map.put("fileName", m.getOriginalFileName());
        map.put("size", m.getFileSize());
        map.put("type", m.getMediaType());
        return map;
    }

    @GetMapping("/download/{mediaId}")
    @Operation(summary = "Dosya indir")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long customerId,
            @PathVariable Long mediaId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media bulunamadı: " + mediaId));

        if (!media.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Bu dosya bu müşteriye ait değil");
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

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + media.getOriginalFileName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Dosya indirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/view/{mediaId}")
    @Operation(summary = "Dosyayı tarayıcıda görüntüle")
    public ResponseEntity<Resource> viewFile(
            @PathVariable Long customerId,
            @PathVariable Long mediaId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        CustomerMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media bulunamadı: " + mediaId));

        if (!media.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Bu dosya bu müşteriye ait değil");
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

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + media.getOriginalFileName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Dosya görüntülenemedi: " + e.getMessage());
        }
    }
}