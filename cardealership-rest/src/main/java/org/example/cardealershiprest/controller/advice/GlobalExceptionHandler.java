package org.example.cardealershiprest.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.example.apicontract.dto.StatusResponse;
import org.example.apicontract.exception.DuplicateResourceException;
import org.example.apicontract.exception.InvalidOperationException;
import org.example.apicontract.exception.ResourceNotFoundException;
import org.example.cardealershiprest.controller.RootController;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestControllerAdvice(basePackages = "org.example.cardealershiprest.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StatusResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<StatusResponse> handleDuplicateResource(DuplicateResourceException ex,
                                                                  HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<StatusResponse> handleInvalidOperation(InvalidOperationException ex,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusResponse> handleValidation(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponse> handleGenericException(Exception ex,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера", request);
    }

    private ResponseEntity<StatusResponse> buildErrorResponse(HttpStatus status,
                                                              String message,
                                                              HttpServletRequest request) {
        StatusResponse response = new StatusResponse("error", message);
        response.add(linkTo(methodOn(RootController.class).getRoot()).withRel("root"));
        response.add(Link.of(request.getRequestURI()).withSelfRel());
        return ResponseEntity.status(status).body(response);
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
