package org.example.apicontract.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s с ID %s не найден", resourceName, resourceId));
    }
}