package com.romanskochko.taxi.core.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PACKAGE;

@Getter
@RequiredArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNKNOWN("unknown_error"),
    WRONG_PASSWORD("wrong_password"),
    SAME_PASSWORD("same_password_as_before"),
    PASSWORD_MISMATCH("passwords_do_not_match"),

    RESOURCE_NOT_FOUND("Resource not found");

    String value;

    @Override
    public String toString() {
        return value;
    }
}
