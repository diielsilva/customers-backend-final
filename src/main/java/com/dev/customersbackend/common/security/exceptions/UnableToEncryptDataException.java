package com.dev.customersbackend.common.security.exceptions;

public class UnableToEncryptDataException extends SecurityException {
    public UnableToEncryptDataException(String message) {
        super(message);
    }
}
