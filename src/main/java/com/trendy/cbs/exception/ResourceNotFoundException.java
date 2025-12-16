package com.trendy.cbs.exception;


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with id " + id);
    }

    public ResourceNotFoundException(String resourceName, String description) {
        super(resourceName + " not found with " +  description);
    }

}

