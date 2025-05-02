package com.filesharepro.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileTypeException extends FileShareException {
    public InvalidFileTypeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}