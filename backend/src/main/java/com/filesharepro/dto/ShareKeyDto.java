package com.filesharepro.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShareKeyDto {
    private String key;
    private Long fileId;
    private LocalDateTime expiresAt;
    private Integer maxUses;
    private Boolean isActive;
    private String fileUrl;
}