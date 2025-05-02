package com.filesharepro.exception;

import org.springframework.http.HttpStatus;

public class FileShareException extends RuntimeException {
    private final HttpStatus status;

    public FileShareException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FileShareException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}