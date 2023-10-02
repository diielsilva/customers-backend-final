package com.dev.customersbackend.common.security.config;

import com.dev.customersbackend.common.security.exceptions.UnableToLoadSecurityValuesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    @Value(value = "${security.aes.key}")
    private String aesKey;

    @Value(value = "${security.token.key}")
    private String tokenKey;

    @Value(value = "${security.token.duration}")
    private Long tokenDuration;

    public String getAesKeyOrThrowsException() {
        if (aesKey == null) {
            throw new UnableToLoadSecurityValuesException("Não foi possível carregar a chave AES.");
        }
        return aesKey;
    }

    public String getTokenKeyOrThrowsException() {
        if (tokenKey == null) {
            throw new UnableToLoadSecurityValuesException("Não foi possível carregar a chave do Token.");
        }
        return tokenKey;
    }

    public Long getTokenDurationOrThrowsException() {
        if (tokenDuration == null) {
            throw new UnableToLoadSecurityValuesException("Não foi possível carregar a duração do Token.");
        }
        return tokenDuration;
    }
}
