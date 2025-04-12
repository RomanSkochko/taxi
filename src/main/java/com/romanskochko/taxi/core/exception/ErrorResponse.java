package com.romanskochko.taxi.core.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@ToString
@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class ErrorResponse {
    OffsetDateTime timestamp;
    int status;
    String message;
    String error;
    Map<String, String> errors;
}
