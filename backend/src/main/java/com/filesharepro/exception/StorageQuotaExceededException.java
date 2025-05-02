package com.filesharepro.exception;

import org.springframework.http.HttpStatus;

public class StorageQuotaExceededException extends FileShareException {
    public StorageQuotaExceededException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}