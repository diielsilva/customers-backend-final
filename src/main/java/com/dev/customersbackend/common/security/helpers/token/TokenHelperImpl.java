package com.dev.customersbackend.common.security.helpers.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dev.customersbackend.common.security.config.SecurityConstants;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenHelperImpl implements TokenHelper {
    private final SecurityConstants constants;

    public TokenHelperImpl(SecurityConstants constants) {
        this.constants = constants;
    }

    @Override
    public String createToken(String username) {
        return JWT.create().withSubject(username).withExpiresAt(getExpirationDate())
                .sign(Algorithm.HMAC512(constants.getTokenKeyOrThrowsException()));
    }

    @Override
    public String validateToken(String token) {
        return JWT.require(Algorithm.HMAC512(constants.getTokenKeyOrThrowsException())).build()
                .verify(token.replace("Bearer ", "")).getSubject();
    }

    @Override
    public Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + constants.getTokenDurationOrThrowsException());
    }
}
