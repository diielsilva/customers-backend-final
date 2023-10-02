package com.dev.customersbackend.common.security.helpers.encryption;

public interface DataEncryptionHelper {
    String encrypt(String property);

    String decrypt(String property);
}
