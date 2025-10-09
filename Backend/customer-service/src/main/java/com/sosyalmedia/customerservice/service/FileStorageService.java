package com.sosyalmedia.customerservice.service;

import com.sosyalmedia.customerservice.config.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final FileUploadProperties fileUploadProperties;

    /**
     * Dosya kaydet - Format: customername-originalfilename.ext
     */
    public String storeFile(MultipartFile file, String companyName, String fileType) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        String fileNameWithoutExt = getFileNameWithoutExtension(originalFileName);

        try {
            String sanitizedCompanyName = sanitizeCompanyName(companyName);

            // Dizin yapısı
            Path companyDirectory = Paths.get(fileUploadProperties.getDirectory())
                    .resolve(sanitizedCompanyName);
            Path uploadPath = companyDirectory.resolve(fileType);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Directory created: {}", uploadPath);
            }

            // Yeni dosya adı: customername-originalfilename.ext
            String newFileName = sanitizedCompanyName + "-" + sanitizeFileName(fileNameWithoutExt) + "." + fileExtension;

            // Aynı isimli dosya varsa sonuna sayı ekle
            newFileName = getUniqueFileName(uploadPath, newFileName);

            Path targetLocation = uploadPath.resolve(newFileName);

            // Dosyayı kaydet
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File stored: {}", targetLocation);

            // DB'ye kaydedilecek: customername/logos/customername-originalfilename.jpg
            return sanitizedCompanyName + "/" + fileType + "/" + newFileName;

        } catch (IOException ex) {
            log.error("Failed to store file: {}", originalFileName, ex);
            throw new RuntimeException("Dosya kaydedilemedi: " + originalFileName, ex);
        }
    }

    public List<String> storeFiles(List<MultipartFile> files, String companyName, String fileType) {
        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String path = storeFile(file, companyName, fileType);
                filePaths.add(path);
            }
        }
        return filePaths;
    }

    public void deleteFile(String relativePath) {
        try {
            Path fullPath = Paths.get(fileUploadProperties.getDirectory()).resolve(relativePath);
            boolean deleted = Files.deleteIfExists(fullPath);

            if (deleted) {
                log.info("File deleted: {}", relativePath);
            } else {
                log.warn("File not found: {}", relativePath);
            }

        } catch (IOException ex) {
            log.error("Could not delete file: {}", relativePath, ex);
            throw new RuntimeException("Dosya silinemedi: " + relativePath, ex);
        }
    }

    /**
     * Şirket adını temizle: "Cafe Sunshine" -> "cafe-sunshine"
     */
    private String sanitizeCompanyName(String companyName) {
        return companyName
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-+", "-");
    }

    /**
     * Dosya adını temizle: "My Photo (1).jpg" -> "my-photo-1"
     */
    private String sanitizeFileName(String fileName) {
        return fileName
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-+", "-");
    }

    /**
     * Aynı isimli dosya varsa sonuna sayı ekle
     * Örnek: logo.jpg -> logo-1.jpg -> logo-2.jpg
     */
    private String getUniqueFileName(Path directory, String fileName) {
        String nameWithoutExt = getFileNameWithoutExtension(fileName);
        String extension = getFileExtension(fileName);

        Path filePath = directory.resolve(fileName);
        int counter = 1;

        while (Files.exists(filePath)) {
            String newFileName = nameWithoutExt + "-" + counter + "." + extension;
            filePath = directory.resolve(newFileName);
            counter++;
        }

        return filePath.getFileName().toString();
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileName.substring(lastDot + 1).toLowerCase();
    }

    private String getFileNameWithoutExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? fileName : fileName.substring(0, lastDot);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Dosya boş olamaz");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.contains("..")) {
            throw new IllegalArgumentException("Geçersiz dosya adı");
        }

        String extension = getFileExtension(originalFileName);
        if (!isAllowedExtension(extension)) {
            throw new IllegalArgumentException(
                    "Desteklenmeyen format: " + extension +
                            ". İzin verilenler: " + fileUploadProperties.getAllowedExtensions()
            );
        }

        long maxSize = 200 * 1024 * 1024; // 200MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "Dosya çok büyük: " + (file.getSize() / 1024 / 1024) + "MB. Max: 200MB"
            );
        }

    }

    private boolean isAllowedExtension(String extension) {
        return fileUploadProperties.getAllowedExtensions()
                .stream()
                .anyMatch(allowed -> allowed.equalsIgnoreCase(extension));
    }

    /**
     * Müşteri klasörünü silinmiş müşteriler dizinine taşı (SOFT DELETE)
     */
    public void moveCustomerFolderToDeleted(String companyName) {
        try {
            String sanitizedCompanyName = sanitizeCompanyName(companyName);

            Path sourceFolder = Paths.get(fileUploadProperties.getDirectory())
                    .resolve(sanitizedCompanyName);

            Path deletedFolder = Paths.get(fileUploadProperties.getDirectory())
                    .resolve("silinmis-musteriler");

            if (!Files.exists(deletedFolder)) {
                Files.createDirectories(deletedFolder);
                log.info("Created deleted customers directory: {}", deletedFolder);
            }

            Path targetFolder = deletedFolder.resolve(sanitizedCompanyName);

            if (Files.exists(sourceFolder)) {
                Files.move(sourceFolder, targetFolder, StandardCopyOption.REPLACE_EXISTING);
                log.info("Moved customer folder to deleted: {} -> {}", sourceFolder, targetFolder);
            } else {
                log.warn("Source folder not found: {}", sourceFolder);
            }

        } catch (IOException ex) {
            log.error("Failed to move customer folder to deleted: {}", companyName, ex);
            throw new RuntimeException("Müşteri klasörü taşınamadı: " + companyName, ex);
        }
    }

    /**
     * Müşteri klasörünü silinmiş müşterilerden geri getir (RESTORE)
     */
    public void restoreCustomerFolder(String companyName) {
        try {
            String sanitizedCompanyName = sanitizeCompanyName(companyName);

            Path deletedFolder = Paths.get(fileUploadProperties.getDirectory())
                    .resolve("silinmis-musteriler")
                    .resolve(sanitizedCompanyName);

            Path targetFolder = Paths.get(fileUploadProperties.getDirectory())
                    .resolve(sanitizedCompanyName);

            if (Files.exists(deletedFolder)) {
                Files.move(deletedFolder, targetFolder, StandardCopyOption.REPLACE_EXISTING);
                log.info("Restored customer folder: {} -> {}", deletedFolder, targetFolder);
            } else {
                log.warn("Deleted folder not found: {}", deletedFolder);
            }

        } catch (IOException ex) {
            log.error("Failed to restore customer folder: {}", companyName, ex);
            throw new RuntimeException("Müşteri klasörü geri yüklenemedi: " + companyName, ex);
        }
    }

    /**
     * Müşteri klasörünü tamamen sil (HARD DELETE)
     */
    public void deleteCustomerFolder(String companyName) {
        try {
            String sanitizedCompanyName = sanitizeCompanyName(companyName);

            Path normalFolder = Paths.get(fileUploadProperties.getDirectory())
                    .resolve(sanitizedCompanyName);

            if (Files.exists(normalFolder)) {
                deleteDirectoryRecursively(normalFolder);
                log.info("Deleted customer folder: {}", normalFolder);
            }

            Path deletedFolder = Paths.get(fileUploadProperties.getDirectory())
                    .resolve("silinmis-musteriler")
                    .resolve(sanitizedCompanyName);

            if (Files.exists(deletedFolder)) {
                deleteDirectoryRecursively(deletedFolder);
                log.info("Deleted customer folder from deleted directory: {}", deletedFolder);
            }

        } catch (IOException ex) {
            log.error("Failed to delete customer folder: {}", companyName, ex);
            throw new RuntimeException("Müşteri klasörü silinemedi: " + companyName, ex);
        }
    }

    /**
     * Klasörü ve içindekileri tamamen sil
     */
    private void deleteDirectoryRecursively(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (Stream<Path> walk = Files.walk(directory)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                log.error("Failed to delete: {}", path, e);
                            }
                        });
            }
        }
    }

}