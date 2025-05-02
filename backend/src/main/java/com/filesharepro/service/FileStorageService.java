package com.filesharepro.service;

import com.filesharepro.dto.FileInfoDto;
import com.filesharepro.dto.FileUpdateRequest;
import com.filesharepro.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    FileInfoDto storeFile(MultipartFile file, User owner);
    Resource loadFileAsResource(String fileKey);
    void deleteFile(String fileKey, User owner);
    List<FileInfoDto> getAllUserFiles(User owner);
    FileInfoDto getFileInfo(String fileKey);
    void updateFileVisibility(String fileKey, User owner, boolean isPublic);
    boolean verifyFileAccess(String fileKey, User user);
    FileInfoDto updateFile(String fileKey, User owner, FileUpdateRequest request);
}