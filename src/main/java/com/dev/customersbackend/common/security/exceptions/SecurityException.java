package com.dev.customersbackend.common.security.exceptions;

public class SecurityException extends RuntimeException {
    private final String message;

    public SecurityException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
