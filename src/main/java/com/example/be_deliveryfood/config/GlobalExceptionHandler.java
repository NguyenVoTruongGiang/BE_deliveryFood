package com.example.be_deliveryfood.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ValidationException;
import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @Autowired
    private MessageSource messageSource;

    private ResponseEntity<ErrorResponse> buildErrorResponse(String errorCode, String defaultMessage, HttpStatus status, String details) {
        String message = messageSource.getMessage(errorCode, null, defaultMessage, LocaleContextHolder.getLocale());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message, details);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        logger.warning("Validation error: " + e.getMessage());
        return buildErrorResponse(
                "error.validation",
                "Validation failed: " + e.getMessage(),
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warning("Invalid argument: " + e.getMessage());
        return buildErrorResponse(
                "error.invalid.argument",
                "Invalid input: " + e.getMessage(),
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.warning("Resource not found: " + e.getMessage());
        return buildErrorResponse(
                "error.not.found",
                "Resource not found: " + e.getMessage(),
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String details = errors.toString();
        logger.warning("Validation failed for request body: " + details);
        return buildErrorResponse(
                "error.validation.request",
                "Invalid request body: " + details,
                HttpStatus.BAD_REQUEST,
                details
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        logger.severe("Unexpected error: " + e.getMessage());
        return buildErrorResponse(
                "error.internal",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }

    static class ErrorResponse {
        private final String errorCode;
        private final String message;
        private final String details;

        public ErrorResponse(String errorCode, String message, String details) {
            this.errorCode = errorCode;
            this.message = message;
            this.details = details;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }
    }
}