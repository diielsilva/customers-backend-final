package com.dev.customersbackend.common.security.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles(value = "local")
class SecurityConstantsTest {
    @Autowired
    private SecurityConstants constants;

    @Test
    void getAesKey_AssertThatAreKey_WhenApplicationFileIsSuccessfullyLoaded() {
        String key = constants.getAesKeyOrThrowsException();
        assertNotNull(key);
        assertEquals("7481c834-1288-45", key);
    }

    @Test
    void getTokenKey_AssertThatAreKey_WhenApplicationProfileIsSuccessfullyLoaded() {
        String key = constants.getTokenKeyOrThrowsException();
        assertNotNull(key);
        assertEquals("9b8879a4-1884-4a98-9a5f-807fd987024b", key);
    }

    @Test
    void getTokenDuration_AssertThatAreDuration_WhenApplicationProfileIsSuccessfullyLoaded() {
        Long duration = constants.getTokenDurationOrThrowsException();
        assertNotNull(duration);
        assertEquals(60000L, duration);
    }

}