package com.filesharepro.exception;

import org.springframework.http.HttpStatus;

public class FileStorageException extends FileShareException {
    public FileStorageException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}