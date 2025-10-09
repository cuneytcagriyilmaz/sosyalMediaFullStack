package com.sosyalmedia.customerservice.service;


import com.sosyalmedia.customerservice.config.FileUploadProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock
    private FileUploadProperties fileUploadProperties;

    @InjectMocks
    private FileStorageService fileStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        lenient().when(fileUploadProperties.getDirectory()).thenReturn(tempDir.toString());
        lenient().when(fileUploadProperties.getAllowedExtensions())
                .thenReturn(Arrays.asList("jpg", "jpeg", "png", "pdf", "mp4"));
    }

    @Test
    void storeFile_Success() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-logo.jpg",
                "image/jpeg",
                "test content".getBytes()
        );

        // When
        String filePath = fileStorageService.storeFile(file, "Test Cafe", "logos");

        // Then
        assertThat(filePath).isNotNull();
        assertThat(filePath).contains("test-cafe/logos/test-cafe-test-logo.jpg");

        Path savedFile = tempDir.resolve(filePath);
        assertThat(Files.exists(savedFile)).isTrue();
    }

    @Test
    void storeFile_CreatesDirectoryIfNotExists() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.png",
                "image/png",
                "test content".getBytes()
        );

        // When
        String filePath = fileStorageService.storeFile(file, "New Company", "photos");

        // Then
        Path companyDir = tempDir.resolve("new-company/photos");
        assertThat(Files.exists(companyDir)).isTrue();
        assertThat(Files.isDirectory(companyDir)).isTrue();
    }

    @Test
    void storeFile_InvalidExtension_ThrowsException() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "malicious.exe",
                "application/octet-stream",
                "malicious content".getBytes()
        );

        // When & Then
        assertThatThrownBy(() -> fileStorageService.storeFile(file, "Test Cafe", "logos"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Desteklenmeyen format");
    }

    @Test
    void storeFile_EmptyFile_ThrowsException() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        // When & Then
        assertThatThrownBy(() -> fileStorageService.storeFile(file, "Test Cafe", "logos"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dosya boş olamaz");
    }

//    @Test
//    void storeFile_FileTooLarge_ThrowsException() {
//        // Given
//        lenient().when(fileUploadProperties.getDirectory()).thenReturn(tempDir.toString());
//        lenient().when(fileUploadProperties.getAllowedExtensions())
//                .thenReturn(Arrays.asList("jpg", "jpeg", "png", "pdf", "mp4"));
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "large.jpg",
//                "image/jpeg",
//                "small content".getBytes()
//        ) {
//            @Override
//            public long getSize() {
//                return 101L * 1024 * 1024; // 101MB (100MB üzeri)
//            }
//        };
//
//        // When & Then
//        assertThatThrownBy(() -> fileStorageService.storeFile(file, "Test Cafe", "logos"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Dosya çok büyük");
//    }

    @Test
    void storeFiles_Success() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "logo1.jpg",
                "image/jpeg",
                "content1".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "logo2.png",
                "image/png",
                "content2".getBytes()
        );
        List<MultipartFile> files = Arrays.asList(file1, file2);

        // When
        List<String> filePaths = fileStorageService.storeFiles(files, "Test Cafe", "logos");

        // Then
        assertThat(filePaths).hasSize(2);
        assertThat(filePaths.get(0)).contains("test-cafe/logos/test-cafe-logo1.jpg");
        assertThat(filePaths.get(1)).contains("test-cafe/logos/test-cafe-logo2.png");
    }

    @Test
    void deleteFile_Success() throws IOException {
        // Given
        Path testFile = tempDir.resolve("test-cafe/logos/test-logo.jpg");
        Files.createDirectories(testFile.getParent());
        Files.createFile(testFile);

        // When
        fileStorageService.deleteFile("test-cafe/logos/test-logo.jpg");

        // Then
        assertThat(Files.exists(testFile)).isFalse();
    }

    @Test
    void moveCustomerFolderToDeleted_Success() throws IOException {
        // Given
        Path customerFolder = tempDir.resolve("test-cafe");
        Path logoFile = customerFolder.resolve("logos/logo.jpg");
        Files.createDirectories(logoFile.getParent());
        Files.createFile(logoFile);

        // When
        fileStorageService.moveCustomerFolderToDeleted("Test Cafe");

        // Then
        Path deletedFolder = tempDir.resolve("silinmis-musteriler/test-cafe");
        assertThat(Files.exists(deletedFolder)).isTrue();
        assertThat(Files.exists(customerFolder)).isFalse();
        assertThat(Files.exists(deletedFolder.resolve("logos/logo.jpg"))).isTrue();
    }

    @Test
    void restoreCustomerFolder_Success() throws IOException {
        // Given
        Path deletedFolder = tempDir.resolve("silinmis-musteriler/test-cafe");
        Path logoFile = deletedFolder.resolve("logos/logo.jpg");
        Files.createDirectories(logoFile.getParent());
        Files.createFile(logoFile);

        // When
        fileStorageService.restoreCustomerFolder("Test Cafe");

        // Then
        Path restoredFolder = tempDir.resolve("test-cafe");
        assertThat(Files.exists(restoredFolder)).isTrue();
        assertThat(Files.exists(deletedFolder)).isFalse();
        assertThat(Files.exists(restoredFolder.resolve("logos/logo.jpg"))).isTrue();
    }

    @Test
    void deleteCustomerFolder_Success() throws IOException {
        // Given
        Path customerFolder = tempDir.resolve("test-cafe");
        Path logoFile = customerFolder.resolve("logos/logo.jpg");
        Files.createDirectories(logoFile.getParent());
        Files.createFile(logoFile);

        // When
        fileStorageService.deleteCustomerFolder("Test Cafe");

        // Then
        assertThat(Files.exists(customerFolder)).isFalse();
    }

    @Test
    void deleteCustomerFolder_DeletesFromBothLocations() throws IOException {
        // Given
        Path normalFolder = tempDir.resolve("test-cafe");
        Path deletedFolder = tempDir.resolve("silinmis-musteriler/test-cafe");

        Files.createDirectories(normalFolder);
        Files.createDirectories(deletedFolder);

        // When
        fileStorageService.deleteCustomerFolder("Test Cafe");

        // Then
        assertThat(Files.exists(normalFolder)).isFalse();
        assertThat(Files.exists(deletedFolder)).isFalse();
    }
}
