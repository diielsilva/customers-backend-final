package com.dev.customersbackend.common.dtos.phone;

public class PhoneResponseDTO {
    private Long id;
    private String number;

    public PhoneResponseDTO(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
