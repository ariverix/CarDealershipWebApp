package org.example.apicontract.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(String.format("%s с %s=%s уже существует", resourceName, field, value));
    }
}
