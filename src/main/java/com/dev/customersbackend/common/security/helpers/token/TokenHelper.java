package com.dev.customersbackend.common.security.helpers.token;

import java.util.Date;

public interface TokenHelper {
    String createToken(String username);

    String validateToken(String token);

    Date getExpirationDate();
}
