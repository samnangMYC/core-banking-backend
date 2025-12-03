package com.trendy.cbs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers. This class provides centralized
 * exception handling for various exceptions that may occur during API requests,
 * returning appropriate HTTP responses with error details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from method argument validation failures.
     * Collects field-specific error messages and returns them in a map.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return a ResponseEntity with HTTP 400 status and a map of field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrorException(
            MethodArgumentNotValidException ex
    ){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles duplicate resource exceptions, such as when attempting to create
     * a resource that already exists. Returns a conflict response with details.
     *
     * @param dr the DuplicationResource exception with the error message
     * @return a ResponseEntity with HTTP 409 status and a map containing error details
     */
    @ExceptionHandler(DuplicationResource.class)
    public ResponseEntity<?> handleDuplicateResource(DuplicationResource dr) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", dr.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles exceptions when a general resource is not found. Returns a not found
     * response with the exception message.
     *
     * @param e the ResourceNotFoundException with the error message
     * @return a ResponseEntity with HTTP 404 status and the error message as body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity
               .status(HttpStatus.NOT_FOUND)
               .body(e.getMessage());
    }
}
