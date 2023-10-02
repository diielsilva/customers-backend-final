package com.dev.customersbackend.common.security.helpers.encryption;

import com.dev.customersbackend.common.security.exceptions.UnableToDecryptDataException;
import com.dev.customersbackend.common.security.exceptions.UnableToEncryptDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "local")
class DataEncryptionHelperTest {
    @Autowired
    private DataEncryptionHelper helper;

    @Test
    void encrypt_AssertValueWasEncrypted_WhenNoOneExceptionIsThrown() {
        String encryptedValue = helper.encrypt("12345");
        assertNotNull(encryptedValue);
        assertNotEquals("12345", encryptedValue);
    }

    @Test
    void encrypt_AssertThatAnExceptionWasThrown_WhenANullValueIsGiven() {
        assertThrows(UnableToEncryptDataException.class, () -> helper.encrypt(null));
    }

    @Test
    void decrypt_AssertValueWasDecrypted_WhenNoOneExceptionIsThrown() {
        String encryptedValue = helper.encrypt("12345");
        String decryptedValue = helper.decrypt(encryptedValue);
        assertNotNull(decryptedValue);
        assertNotEquals(encryptedValue, decryptedValue);
        assertEquals("12345", decryptedValue);
    }

    @Test
    void decrypt_AssertThatAnExceptionWasThrown_WhenANullValueIsGiven() {
        assertThrows(UnableToDecryptDataException.class, () -> helper.decrypt(null));
    }
}