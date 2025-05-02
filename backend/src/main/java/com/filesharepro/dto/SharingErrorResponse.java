package com.filesharepro.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class SharingErrorResponse extends ErrorResponse {
    private Long fileId;
    private String shareKey;
    private LocalDateTime keyExpiryDate;
    private Integer remainingUses;

    public SharingErrorResponse(int status, String error, String message, String path) {
        super(status, error, message, path);
    }

    public SharingErrorResponse(int status, String error, String message, String path, 
            Long fileId, String shareKey, LocalDateTime keyExpiryDate, Integer remainingUses) {
        super(status, error, message, path);
        this.fileId = fileId;
        this.shareKey = shareKey;
        this.keyExpiryDate = keyExpiryDate;
        this.remainingUses = remainingUses;
    }
}