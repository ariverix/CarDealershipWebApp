package org.example.apicontract.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(String.format("%s с %s=%s уже существует", resourceName, field, value));
    }
}