package com.filesharepro.service;

import com.filesharepro.dto.ShareKeyDto;
import com.filesharepro.entity.FileMetadata;
import com.filesharepro.entity.SharingKey;
import com.filesharepro.entity.User;
import com.filesharepro.exception.InvalidSharingKeyException;
import com.filesharepro.exception.ResourceNotFoundException;
import com.filesharepro.exception.FileShareException;
import com.filesharepro.repository.FileMetadataRepository;
import com.filesharepro.repository.SharingKeyRepository;
import com.filesharepro.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SharingServiceImpl implements SharingService {

    @Autowired
    private SharingKeyRepository sharingKeyRepository;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private KeyGenerator keyGenerator;

    @Override
    @Transactional
    public ShareKeyDto createShareKey(Long fileId, User owner, LocalDateTime expiresAt, Integer maxUses) {
        FileMetadata file = fileMetadataRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("File", fileId.toString()));

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new FileShareException("Not authorized to share this file", HttpStatus.FORBIDDEN);
        }

        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            throw new InvalidSharingKeyException("Expiry date must be in the future", null, expiresAt, null);
        }

        if (maxUses != null && maxUses < 1) {
            throw new InvalidSharingKeyException("Maximum uses must be at least 1", null, null, maxUses);
        }

        String key;
        boolean keyExists;
        do {
            key = keyGenerator.generateKey();
            keyExists = sharingKeyRepository.findByKeyAndIsActiveTrue(key).isPresent();
        } while (keyExists);

        SharingKey sharingKey = new SharingKey();
        sharingKey.setKey(key);
        sharingKey.setFile(file);
        sharingKey.setExpiresAt(expiresAt);
        sharingKey.setMaxUses(maxUses);
        sharingKey.setUsageCount(0);
        sharingKey.setIsActive(true);

        sharingKey = sharingKeyRepository.save(sharingKey);
        return convertToDto(sharingKey);
    }

    @Override
    public ShareKeyDto getShareKey(String key) {
        SharingKey sharingKey = sharingKeyRepository.findByKeyAndIsActiveTrue(key)
            .orElseThrow(() -> new InvalidSharingKeyException("Share key not found or inactive", key, null, null));

        if (sharingKey.getExpiresAt() != null && sharingKey.getExpiresAt().isBefore(LocalDateTime.now())) {
            sharingKey.setIsActive(false);
            sharingKeyRepository.save(sharingKey);
            throw new InvalidSharingKeyException("Share key has expired", 
                key, sharingKey.getExpiresAt(), null);
        }

        if (sharingKey.getMaxUses() != null && sharingKey.getUsageCount() >= sharingKey.getMaxUses()) {
            sharingKey.setIsActive(false);
            sharingKeyRepository.save(sharingKey);
            throw new InvalidSharingKeyException("Share key has reached maximum uses", 
                key, null, sharingKey.getMaxUses());
        }

        sharingKey.setUsageCount(sharingKey.getUsageCount() + 1);
        sharingKey = sharingKeyRepository.save(sharingKey);
        
        return convertToDto(sharingKey);
    }

    @Override
    public List<ShareKeyDto> getFileShareKeys(Long fileId, User owner) {
        FileMetadata file = fileMetadataRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("File", fileId.toString()));

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new FileShareException("Not authorized to view sharing keys for this file", HttpStatus.FORBIDDEN);
        }

        return file.getSharingKeys().stream()
            .filter(SharingKey::getIsActive)
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void revokeShareKey(String key, User owner) {
        SharingKey sharingKey = sharingKeyRepository.findByKeyAndIsActiveTrue(key)
            .orElseThrow(() -> new InvalidSharingKeyException("Share key not found or inactive", key, null, null));

        if (!sharingKey.getFile().getOwner().getId().equals(owner.getId())) {
            throw new FileShareException("Not authorized to revoke this share key", HttpStatus.FORBIDDEN);
        }

        sharingKey.setIsActive(false);
        sharingKeyRepository.save(sharingKey);
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void cleanupExpiredKeys() {
        sharingKeyRepository.deleteByExpiresAtBeforeAndExpiresAtIsNotNull(LocalDateTime.now());
    }

    private ShareKeyDto convertToDto(SharingKey sharingKey) {
        if (sharingKey == null) {
            return null;
        }
        ShareKeyDto dto = new ShareKeyDto();
        dto.setKey(sharingKey.getKey());
        dto.setFileId(sharingKey.getFile().getId());
        dto.setExpiresAt(sharingKey.getExpiresAt());
        dto.setMaxUses(sharingKey.getMaxUses());
        dto.setIsActive(sharingKey.getIsActive());
        dto.setFileUrl("/api/files/" + sharingKey.getFile().getFileKey());
        return dto;
    }
}