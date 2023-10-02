package com.dev.customersbackend.common.security.exceptions;

public class BadCredentialsException extends RuntimeException {
    private final String message;

    public BadCredentialsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
