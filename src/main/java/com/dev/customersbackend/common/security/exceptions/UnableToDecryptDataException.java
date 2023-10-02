package com.dev.customersbackend.common.security.exceptions;

public class UnableToDecryptDataException extends SecurityException {
    public UnableToDecryptDataException(String message) {
        super(message);
    }
}
