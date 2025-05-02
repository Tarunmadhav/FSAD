package com.filesharepro.exception;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class InvalidSharingKeyException extends FileShareException {
    private final String shareKey;
    private final LocalDateTime expiryDate;
    private final Integer remainingUses;

    public InvalidSharingKeyException(String message, String shareKey, LocalDateTime expiryDate, Integer remainingUses) {
        super(message, HttpStatus.BAD_REQUEST);
        this.shareKey = shareKey;
        this.expiryDate = expiryDate;
        this.remainingUses = remainingUses;
    }

    public String getShareKey() {
        return shareKey;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public Integer getRemainingUses() {
        return remainingUses;
    }
}