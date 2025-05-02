package com.filesharepro.repository;

import com.filesharepro.entity.FileMetadata;
import com.filesharepro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findByOwner(User owner);
    Optional<FileMetadata> findByFileKey(String fileKey);
    List<FileMetadata> findByOwnerAndFilenameContainingIgnoreCase(User owner, String filename);
    boolean existsByFileKey(String fileKey);
}