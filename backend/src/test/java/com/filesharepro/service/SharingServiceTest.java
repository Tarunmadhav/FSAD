package com.filesharepro.service;

import com.filesharepro.entity.FileMetadata;
import com.filesharepro.entity.SharingKey;
import com.filesharepro.entity.User;
import com.filesharepro.repository.FileMetadataRepository;
import com.filesharepro.repository.SharingKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SharingServiceTest {

    @Autowired
    private SharingService sharingService;

    @MockBean
    private SharingKeyRepository sharingKeyRepository;

    @MockBean
    private FileMetadataRepository fileMetadataRepository;

    private User testUser;
    private FileMetadata testFile;
    private SharingKey testKey;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testFile = new FileMetadata();
        testFile.setId(1L);
        testFile.setFilename("test.txt");
        testFile.setOwner(testUser);
        testFile.setFileKey("test-file-key");

        testKey = new SharingKey();
        testKey.setId(1L);
        testKey.setKey("test-share-key");
        testKey.setFile(testFile);
        testKey.setCreatedAt(LocalDateTime.now());
        testKey.setIsActive(true);
    }

    @Test
    void createShareKey_WithValidData_ShouldSucceed() {
        when(fileMetadataRepository.findById(1L)).thenReturn(Optional.of(testFile));
        when(sharingKeyRepository.save(any())).thenReturn(testKey);

        var shareKey = sharingService.createShareKey(1L, testUser, LocalDateTime.now().plusDays(1), 10);
        
        assertNotNull(shareKey);
        assertEquals(testFile.getId(), shareKey.getFileId());
        assertTrue(shareKey.getIsActive());
    }

    @Test
    void createShareKey_WithNonExistentFile_ShouldThrowException() {
        when(fileMetadataRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            sharingService.createShareKey(1L, testUser, null, null);
        });
    }

    @Test
    void getShareKey_WithValidKey_ShouldSucceed() {
        when(sharingKeyRepository.findByKeyAndIsActiveTrue("test-share-key"))
            .thenReturn(Optional.of(testKey));
        when(sharingKeyRepository.save(any(SharingKey.class)))
            .thenReturn(testKey);

        var shareKey = sharingService.getShareKey("test-share-key");
        
        assertNotNull(shareKey);
        assertEquals(testFile.getId(), shareKey.getFileId());
        verify(sharingKeyRepository).save(any(SharingKey.class));
    }

    @Test
    void getShareKey_WithExpiredKey_ShouldThrowException() {
        testKey.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(sharingKeyRepository.findByKeyAndIsActiveTrue("test-share-key"))
            .thenReturn(Optional.of(testKey));

        assertThrows(RuntimeException.class, () -> {
            sharingService.getShareKey("test-share-key");
        });

        verify(sharingKeyRepository).save(testKey);
        assertFalse(testKey.getIsActive());
    }

    @Test
    void revokeShareKey_AsOwner_ShouldSucceed() {
        when(sharingKeyRepository.findByKeyAndIsActiveTrue("test-share-key"))
            .thenReturn(Optional.of(testKey));

        sharingService.revokeShareKey("test-share-key", testUser);
        
        verify(sharingKeyRepository).save(testKey);
        assertFalse(testKey.getIsActive());
    }

    @Test
    void revokeShareKey_AsNonOwner_ShouldThrowException() {
        User otherUser = new User();
        otherUser.setId(2L);

        when(sharingKeyRepository.findByKeyAndIsActiveTrue("test-share-key"))
            .thenReturn(Optional.of(testKey));

        assertThrows(RuntimeException.class, () -> {
            sharingService.revokeShareKey("test-share-key", otherUser);
        });
    }
}