package org.example.cardealershiprest.exception;

import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.exception.DuplicateResourceException;
import org.example.apicontract.exception.InvalidOperationException;
import org.example.apicontract.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Shared exception handler for the REST layer. Maps domain exceptions to the unified {@link StatusResponse} payload.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StatusResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<StatusResponse> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<StatusResponse> handleInvalidOperation(InvalidOperationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StatusResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StatusResponse("error", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StatusResponse("error", "Внутренняя ошибка сервера"));
    }
}
