package com.dev.customersbackend.common.security.helpers.token;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "local")
class TokenHelperTest {
    @Autowired
    private TokenHelper helper;

    @Test
    void createToken_AssertThatAreToken_WhenNoOneExceptionWasThrown() {
        String token = helper.createToken("oliver");
        assertNotNull(token);
    }

    @Test
    void validateToken_AssertTokenIsValid_WhenNoOneExceptionWasThrown() {
        String token = helper.createToken("oliver");
        String username = helper.validateToken(token);
        assertNotNull(username);
    }

    @Test
    void getExpirationDate_AssertExpirationDateIsValid_WhenNoOneExceptionWasThrown() {
        Date date = helper.getExpirationDate();
        assertNotNull(date);
    }
}