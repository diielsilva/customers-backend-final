package com.dev.customersbackend.common.dtos.customer;

import com.dev.customersbackend.common.dtos.address.AddressRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CustomerRequestDTO {
    @NotBlank(message = "O nome não pode ser nulo.")
    private String name;

    @NotBlank(message = "O telefone não pode ser nulo.")
    @Pattern(regexp = "([0-9]{2,3})?([0-9]{2})([0-9]{4,5})([0-9]{4})", message = "O telefone é inválido.")
    private String phoneNumber;

    @NotNull(message = "O endereço não pode ser nulo.")
    @Valid
    private AddressRequestDTO address;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento é inválida.")
    private LocalDate birthDate;

    public CustomerRequestDTO() {
    }

    public CustomerRequestDTO(String name, String phoneNumber, AddressRequestDTO address, LocalDate birthDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AddressRequestDTO getAddress() {
        return address;
    }

    public void setAddress(AddressRequestDTO address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
