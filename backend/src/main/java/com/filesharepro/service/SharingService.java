package com.filesharepro.service;

import com.filesharepro.dto.ShareKeyDto;
import com.filesharepro.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface SharingService {
    ShareKeyDto createShareKey(Long fileId, User owner, LocalDateTime expiresAt, Integer maxUses);
    ShareKeyDto getShareKey(String key);
    List<ShareKeyDto> getFileShareKeys(Long fileId, User owner);
    void revokeShareKey(String key, User owner);
    void cleanupExpiredKeys();
}