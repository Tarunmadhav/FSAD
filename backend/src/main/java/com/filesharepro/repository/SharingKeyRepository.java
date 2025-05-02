package com.filesharepro.repository;

import com.filesharepro.entity.SharingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SharingKeyRepository extends JpaRepository<SharingKey, Long> {
    Optional<SharingKey> findByKeyAndIsActiveTrue(String key);
    void deleteByExpiresAtBeforeAndExpiresAtIsNotNull(LocalDateTime dateTime);
    void deleteByFileId(Long fileId);
}