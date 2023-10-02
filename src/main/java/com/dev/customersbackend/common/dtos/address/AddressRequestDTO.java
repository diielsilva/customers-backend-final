package com.dev.customersbackend.common.dtos.address;


import com.dev.customersbackend.common.validators.cep.CEP;
import jakarta.validation.constraints.NotBlank;

public class AddressRequestDTO {
    @NotBlank(message = "O número da casa não pode ser nulo.")
    private String houseNumber;

    @NotBlank(message = "A rua não pode ser nula.")
    private String street;

    @NotBlank(message = "O bairro não pode ser nulo.")
    private String neighborhood;

    @NotBlank(message = "A cidade não pode ser nula.")
    private String city;

    @NotBlank(message = "O estado não pode ser nulo.")
    private String state;

    @CEP
    private String cep;

    public AddressRequestDTO() {
    }

    public AddressRequestDTO(String houseNumber, String street, String neighborhood, String city, String state, String cep) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.cep = cep;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
