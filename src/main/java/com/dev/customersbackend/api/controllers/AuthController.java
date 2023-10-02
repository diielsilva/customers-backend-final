package com.dev.customersbackend.api.controllers;

import com.dev.customersbackend.common.security.dtos.UserAccessResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserCredentialsRequestDTO;
import com.dev.customersbackend.common.security.services.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<UserAccessResponseDTO> auth(@RequestBody @Valid UserCredentialsRequestDTO userCredentials) {

        UserAccessResponseDTO userAccess = authService.attemptAuthenticate(userCredentials);

        return new ResponseEntity<>(userAccess, addTokenAtHeaders(userAccess.getToken()), HttpStatus.OK);
    }

    private HttpHeaders addTokenAtHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return headers;
    }
}
