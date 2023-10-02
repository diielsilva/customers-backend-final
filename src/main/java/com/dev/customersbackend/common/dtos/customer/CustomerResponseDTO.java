package com.dev.customersbackend.common.dtos.customer;

import com.dev.customersbackend.common.dtos.address.AddressResponseDTO;
import com.dev.customersbackend.common.dtos.phone.PhoneResponseDTO;

import java.time.LocalDate;

public class CustomerResponseDTO {
    private Long id;
    private String name;
    private PhoneResponseDTO phone;
    private AddressResponseDTO address;
    private LocalDate birthDate;

    public CustomerResponseDTO(Long id, String name, PhoneResponseDTO phone, AddressResponseDTO address, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneResponseDTO getPhone() {
        return phone;
    }

    public void setPhone(PhoneResponseDTO phone) {
        this.phone = phone;
    }

    public AddressResponseDTO getAddress() {
        return address;
    }

    public void setAddress(AddressResponseDTO address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
