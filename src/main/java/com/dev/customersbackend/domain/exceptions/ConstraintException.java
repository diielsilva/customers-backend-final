package com.dev.customersbackend.domain.exceptions;

public class ConstraintException extends RuntimeException {
    private final String message;

    public ConstraintException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
