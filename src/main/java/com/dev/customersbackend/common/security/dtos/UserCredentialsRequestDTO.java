package com.dev.customersbackend.common.security.dtos;

import jakarta.validation.constraints.NotBlank;

public class UserCredentialsRequestDTO {
    @NotBlank(message = "O usuário não pode ser nulo.")
    private String username;

    @NotBlank(message = "A senha não pode ser nula.")
    private String password;

    public UserCredentialsRequestDTO() {
    }

    public UserCredentialsRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
