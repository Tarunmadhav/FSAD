package com.filesharepro.service;

import com.filesharepro.entity.FileMetadata;
import com.filesharepro.entity.User;
import com.filesharepro.repository.FileMetadataRepository;
import com.filesharepro.config.FileUploadConfig;
import com.filesharepro.exception.FileStorageException;
import com.filesharepro.exception.InvalidFileTypeException;
import com.filesharepro.exception.StorageQuotaExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileStorageServiceTest {

    @Autowired
    private FileStorageService fileStorageService;

    @MockBean
    private FileMetadataRepository fileMetadataRepository;

    @MockBean
    private FileUploadConfig fileUploadConfig;

    @TempDir
    Path tempDir;

    private User testUser;
    private FileMetadata testFile;
    private MultipartFile multipartFile;
    private MultipartFile largeFile;
    private MultipartFile invalidTypeFile;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setStorageLimit(1073741824L); // 1GB
        testUser.setStorageUsed(0L);

        multipartFile = new MockMultipartFile(
            "test.txt",
            "test.txt",
            "text/plain",
            "Hello, World!".getBytes()
        );

        // Create a mock large file
        largeFile = new MockMultipartFile(
            "large.txt",
            "large.txt",
            "text/plain",
            "Large file content".getBytes()
        );

        // Create a mock file with invalid type
        invalidTypeFile = new MockMultipartFile(
            "test.exe",
            "test.exe",
            "application/x-msdownload",
            "Invalid content".getBytes()
        );

        testFile = new FileMetadata();
        testFile.setId(1L);
        testFile.setFilename("test.txt");
        testFile.setOriginalFilename("test.txt");
        testFile.setContentType("text/plain");
        testFile.setSize(13L);
        testFile.setOwner(testUser);
        testFile.setFileKey("test-key");
        testFile.setUploadDate(LocalDateTime.now());

        // Configure FileUploadConfig mock
        when(fileUploadConfig.getMaxFileSize()).thenReturn(52428800L); // 50MB
        when(fileUploadConfig.getAllowedTypes()).thenReturn(Set.of(
            "text/plain",
            "image/jpeg",
            "application/pdf"
        ));
    }

    @Test
    void storeFile_WithValidFile_ShouldSucceed() {
        when(fileMetadataRepository.save(any())).thenReturn(testFile);

        var fileInfo = fileStorageService.storeFile(multipartFile, testUser);
        
        assertNotNull(fileInfo);
        assertEquals("test.txt", fileInfo.getOriginalFilename());
        assertEquals(13L, fileInfo.getSize());
    }

    @Test
    void storeFile_WithOversizedFile_ShouldThrowException() {
        MockMultipartFile mockLargeFile = Mockito.mock(MockMultipartFile.class);
        when(mockLargeFile.getSize()).thenReturn(52428801L); // 50MB + 1 byte
        when(mockLargeFile.getOriginalFilename()).thenReturn("large.txt");
        when(mockLargeFile.getContentType()).thenReturn("text/plain");

        assertThrows(FileStorageException.class, () -> {
            fileStorageService.storeFile(mockLargeFile, testUser);
        });
    }

    @Test
    void storeFile_WithInvalidFileType_ShouldThrowException() {
        assertThrows(InvalidFileTypeException.class, () -> {
            fileStorageService.storeFile(invalidTypeFile, testUser);
        });
    }

    @Test
    void storeFile_WhenStorageLimitExceeded_ShouldThrowException() {
        testUser.setStorageUsed(testUser.getStorageLimit());

        assertThrows(StorageQuotaExceededException.class, () -> {
            fileStorageService.storeFile(multipartFile, testUser);
        });
    }

    @Test
    void getAllUserFiles_ShouldReturnUserFiles() {
        List<FileMetadata> userFiles = Arrays.asList(testFile);
        when(fileMetadataRepository.findByOwner(testUser)).thenReturn(userFiles);

        var files = fileStorageService.getAllUserFiles(testUser);
        
        assertNotNull(files);
        assertFalse(files.isEmpty());
        assertEquals(1, files.size());
        assertEquals("test.txt", files.get(0).getOriginalFilename());
    }

    @Test
    void verifyFileAccess_WithOwner_ShouldAllowAccess() {
        when(fileMetadataRepository.findByFileKey("test-key"))
            .thenReturn(Optional.of(testFile));

        boolean hasAccess = fileStorageService.verifyFileAccess("test-key", testUser);
        
        assertTrue(hasAccess);
    }

    @Test
    void verifyFileAccess_WithNonOwner_ShouldDenyAccess() {
        User otherUser = new User();
        otherUser.setId(2L);
        
        when(fileMetadataRepository.findByFileKey("test-key"))
            .thenReturn(Optional.of(testFile));

        boolean hasAccess = fileStorageService.verifyFileAccess("test-key", otherUser);
        
        assertFalse(hasAccess);
    }
}