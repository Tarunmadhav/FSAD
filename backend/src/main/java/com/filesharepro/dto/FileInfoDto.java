package com.filesharepro.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileInfoDto {
    private Long id;
    private String filename;
    private String originalFilename;
    private String contentType;
    private Long size;
    private LocalDateTime uploadDate;
    private String fileKey;
    private boolean isPublic;
    private LocalDateTime expiryDate;
}