package com.romanskochko.taxi.core.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CustomApplicationException ex) {
        HttpStatus status = getHttpStatus(ex);

        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), ex.getCode().getValue(), ex.getErrors());

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(EntityNotFoundException ex) {
        String message = ex.getMessage();
        String error = ex.getClass().getSimpleName();
        ErrorResponse errorResponse = buildErrorResponse(NOT_FOUND, message, error, Collections.emptyMap());

        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    private static HttpStatus getHttpStatus(CustomApplicationException ex) {
        return switch (ex.getCode()) {
            case WRONG_PASSWORD, ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case SAME_PASSWORD, PASSWORD_MISMATCH -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private static ErrorResponse buildErrorResponse(HttpStatus status, String message, String error, Map<String, String> errors) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .message(message)
                .error(error)
                .errors(errors)
                .build();
    }
}

