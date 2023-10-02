package com.dev.customersbackend.common.security.services.auth;

import com.dev.customersbackend.common.security.dtos.UserAccessResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserCredentialsRequestDTO;
import com.dev.customersbackend.common.security.exceptions.BadCredentialsException;
import com.dev.customersbackend.common.security.helpers.token.TokenHelper;
import com.dev.customersbackend.domain.entities.User;
import com.dev.customersbackend.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenHelper tokenHelper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, TokenHelper tokenHelper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenHelper = tokenHelper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccessResponseDTO attemptAuthenticate(UserCredentialsRequestDTO userCredentialsDTO) {
        Optional<User> model = userRepository.findByUsername(userCredentialsDTO.getUsername());
        if (model.isEmpty()) {
            throw new BadCredentialsException("Usuário ou senha inválido(a).");
        }
        if (!passwordEncoder.matches(userCredentialsDTO.getPassword(), model.get().getPassword())) {
            throw new BadCredentialsException("Usuário ou senha inválido(a).");
        }
        String token = tokenHelper.createToken(model.get().getUsername());
        return new UserAccessResponseDTO(token);
    }
}
