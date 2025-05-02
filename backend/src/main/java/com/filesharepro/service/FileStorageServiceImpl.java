package com.filesharepro.service;

import com.filesharepro.config.FileUploadConfig;
import com.filesharepro.dto.FileInfoDto;
import com.filesharepro.dto.FileUpdateRequest;
import com.filesharepro.entity.FileMetadata;
import com.filesharepro.entity.User;
import com.filesharepro.exception.FileShareException;
import com.filesharepro.exception.FileStorageException;
import com.filesharepro.exception.InvalidFileTypeException;
import com.filesharepro.exception.ResourceNotFoundException;
import com.filesharepro.exception.StorageQuotaExceededException;
import com.filesharepro.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    private final FileMetadataRepository fileMetadataRepository;
    private final FileUploadConfig fileUploadConfig;

    @Autowired
    public FileStorageServiceImpl(
            @Value("${app.file.upload-dir}") String uploadDir,
            FileMetadataRepository fileMetadataRepository,
            FileUploadConfig fileUploadConfig) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileUploadConfig = fileUploadConfig;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public FileInfoDto storeFile(MultipartFile file, User owner) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileKey = UUID.randomUUID().toString();
        String filename = fileKey + "_" + originalFilename;

        try {
            if (filename.contains("..")) {
                throw new FileStorageException("Invalid file path sequence in filename: " + filename);
            }

            // Validate file size
            if (file.getSize() > fileUploadConfig.getMaxFileSize()) {
                throw new FileStorageException("File size exceeds the maximum allowed size of " + 
                    (fileUploadConfig.getMaxFileSize() / (1024 * 1024)) + "MB");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (!fileUploadConfig.getAllowedTypes().contains(contentType)) {
                throw new InvalidFileTypeException("File type not allowed. Allowed types are: " + 
                    String.join(", ", fileUploadConfig.getAllowedTypes()));
            }

            // Check storage quota
            if (owner.getStorageUsed() + file.getSize() > owner.getStorageLimit()) {
                throw new StorageQuotaExceededException(String.format(
                    "Storage quota exceeded. Available space: %d MB, Required space: %d MB",
                    (owner.getStorageLimit() - owner.getStorageUsed()) / (1024 * 1024),
                    file.getSize() / (1024 * 1024)
                ));
            }

            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileMetadata metadata = new FileMetadata();
            metadata.setFilename(filename);
            metadata.setOriginalFilename(originalFilename);
            metadata.setContentType(contentType);
            metadata.setSize(file.getSize());
            metadata.setOwner(owner);
            metadata.setFileKey(fileKey);
            metadata = fileMetadataRepository.save(metadata);

            owner.setStorageUsed(owner.getStorageUsed() + file.getSize());
            // Note: UserRepository.save(owner) would be called here in a complete implementation

            return convertToDto(metadata);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + filename, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileKey) {
        try {
            FileMetadata metadata = fileMetadataRepository.findByFileKey(fileKey)
                .orElseThrow(() -> new FileStorageException("File not found with key: " + fileKey));

            Path filePath = this.fileStorageLocation.resolve(metadata.getFilename()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found: " + metadata.getFilename());
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found with key: " + fileKey, ex);
        }
    }

    @Override
    public void deleteFile(String fileKey, User owner) {
        FileMetadata metadata = fileMetadataRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new FileStorageException("File not found with key: " + fileKey));

        if (!metadata.getOwner().getId().equals(owner.getId())) {
            throw new FileShareException("Not authorized to delete this file", HttpStatus.FORBIDDEN);
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(metadata.getFilename()).normalize();
            Files.deleteIfExists(filePath);
            owner.setStorageUsed(owner.getStorageUsed() - metadata.getSize());
            fileMetadataRepository.delete(metadata);
            // Note: UserRepository.save(owner) would be called here in a complete implementation
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + metadata.getFilename(), ex);
        }
    }

    @Override
    public List<FileInfoDto> getAllUserFiles(User owner) {
        return fileMetadataRepository.findByOwner(owner)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public FileInfoDto getFileInfo(String fileKey) {
        return fileMetadataRepository.findByFileKey(fileKey)
            .map(this::convertToDto)
            .orElseThrow(() -> new FileStorageException("File not found with key: " + fileKey));
    }

    @Override
    public void updateFileVisibility(String fileKey, User owner, boolean isPublic) {
        FileMetadata metadata = fileMetadataRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new FileStorageException("File not found with key: " + fileKey));

        if (!metadata.getOwner().getId().equals(owner.getId())) {
            throw new FileShareException("Not authorized to update this file", HttpStatus.FORBIDDEN);
        }

        metadata.setPublic(isPublic);
        fileMetadataRepository.save(metadata);
    }

    @Override
    public boolean verifyFileAccess(String fileKey, User user) {
        FileMetadata metadata = fileMetadataRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new FileStorageException("File not found with key: " + fileKey));

        return metadata.isPublic() || metadata.getOwner().getId().equals(user.getId());
    }

    @Override
    @Transactional
    public FileInfoDto updateFile(String fileKey, User owner, FileUpdateRequest request) {
        FileMetadata metadata = fileMetadataRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new ResourceNotFoundException("File", fileKey));

        if (!metadata.getOwner().getId().equals(owner.getId())) {
            throw new FileShareException("Not authorized to update this file", HttpStatus.FORBIDDEN);
        }

        if (request.getFilename() != null) {
            metadata.setOriginalFilename(request.getFilename());
        }

        if (request.getIsPublic() != null) {
            metadata.setPublic(request.getIsPublic());
        }

        if (request.getExpiryDate() != null) {
            if (request.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new FileShareException("Expiry date must be in the future", HttpStatus.BAD_REQUEST);
            }
            metadata.setExpiryDate(request.getExpiryDate());
        }

        metadata = fileMetadataRepository.save(metadata);
        return convertToDto(metadata);
    }

    private FileInfoDto convertToDto(FileMetadata metadata) {
        FileInfoDto dto = new FileInfoDto();
        dto.setId(metadata.getId());
        dto.setFilename(metadata.getOriginalFilename());
        dto.setOriginalFilename(metadata.getOriginalFilename());
        dto.setContentType(metadata.getContentType());
        dto.setSize(metadata.getSize());
        dto.setUploadDate(metadata.getUploadDate());
        dto.setFileKey(metadata.getFileKey());
        dto.setPublic(metadata.isPublic());
        dto.setExpiryDate(metadata.getExpiryDate());
        return dto;
    }
}