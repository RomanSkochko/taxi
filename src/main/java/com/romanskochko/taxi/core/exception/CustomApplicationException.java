package com.romanskochko.taxi.core.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomApplicationException extends RuntimeException {
    ErrorCode code;
    Map<String, String> errors;

    public CustomApplicationException(String message, ErrorCode code) {
        super(message);
        this.code = code;
        this.errors = null;
    }

    public CustomApplicationException(String message, ErrorCode code, Map<String, String> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }

    public static CustomApplicationException of(String message) {
        return new CustomApplicationException(message, null);
    }

    public static CustomApplicationException of(String message, ErrorCode code) {
        return new CustomApplicationException(message, code);
    }

    public static CustomApplicationException of(String message, ErrorCode code, Map<String, String> errors) {
        return new CustomApplicationException(message, code, errors);
    }

}

