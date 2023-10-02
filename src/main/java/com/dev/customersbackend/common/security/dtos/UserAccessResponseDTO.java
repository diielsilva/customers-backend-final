package com.dev.customersbackend.common.security.dtos;

public class UserAccessResponseDTO {
    private String token;

    public UserAccessResponseDTO() {
    }

    public UserAccessResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
