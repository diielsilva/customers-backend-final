package com.dev.customersbackend.common.security.services.auth;

import com.dev.customersbackend.common.security.dtos.UserAccessResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserCredentialsRequestDTO;

public interface AuthService {
    UserAccessResponseDTO attemptAuthenticate(UserCredentialsRequestDTO dto);
}
