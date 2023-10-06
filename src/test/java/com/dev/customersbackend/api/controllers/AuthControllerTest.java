package com.dev.customersbackend.api.controllers;

import com.dev.customersbackend.common.dtos.error.ErrorResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserAccessResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserCredentialsRequestDTO;
import com.dev.customersbackend.domain.entities.User;
import com.dev.customersbackend.domain.factories.UserFactory;
import com.dev.customersbackend.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setup() {
        User user = UserFactory.getOliverWithoutId();
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Test
    void attemptAuthenticate_AssertThatUserWasAuthenticated_WhenNoOneExceptionWasThrown() {

        ResponseEntity<UserAccessResponseDTO> authResponse =
                restTemplate.postForEntity("/auth", UserFactory.getUserCredentials(), UserAccessResponseDTO.class);
        assertNotNull(authResponse);
        assertNotNull(authResponse.getBody());
        assertEquals(HttpStatus.OK, authResponse.getStatusCode());
    }

    @Test
    void attemptAuthenticate_AssertThatAnExceptionWasThrown_WhenPasswordIsIncorrect() {
        UserCredentialsRequestDTO userCredentials = UserFactory.getUserCredentials();
        userCredentials.setPassword("123456");
        ResponseEntity<ErrorResponseDTO> authResponse =
                restTemplate.postForEntity("/auth", userCredentials, ErrorResponseDTO.class);
        assertNotNull(authResponse);
        assertNotNull(authResponse.getBody());
        assertEquals("Usuário ou senha inválido(a).", authResponse.getBody().getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, authResponse.getStatusCode());
    }

    @Test
    void attemptAuthenticate_AssertThatAnExceptionWasThrown_WhenUserIsNotFound() {
        UserCredentialsRequestDTO userCredentials = UserFactory.getUserCredentials();
        userCredentials.setUsername("xyz");
        ResponseEntity<ErrorResponseDTO> authResponse =
                restTemplate.postForEntity("/auth", userCredentials, ErrorResponseDTO.class);
        assertNotNull(authResponse);
        assertNotNull(authResponse.getBody());
        assertEquals("Usuário ou senha inválido(a).", authResponse.getBody().getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, authResponse.getStatusCode());
    }
}