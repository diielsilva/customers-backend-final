package com.dev.customersbackend.domain.factories;

import com.dev.customersbackend.common.security.dtos.UserCredentialsRequestDTO;
import com.dev.customersbackend.domain.entities.User;
import com.dev.customersbackend.domain.enums.Permission;

public class UserFactory {
    private UserFactory() {

    }

    public static User getOliverWithoutId() {
        return new User("Oliver Bernardo Melo", "oliver", "12345", Permission.ADMIN);
    }

    public static UserCredentialsRequestDTO getUserCredentials() {
        return new UserCredentialsRequestDTO("daniel", "12345");
    }
}
