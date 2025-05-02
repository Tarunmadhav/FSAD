package com.filesharepro.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends FileShareException {
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(resourceType + " not found with identifier: " + identifier, HttpStatus.NOT_FOUND);
    }
}