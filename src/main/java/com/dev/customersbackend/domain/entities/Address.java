package com.dev.customersbackend.domain.entities;

import com.dev.customersbackend.common.security.helpers.converter.DataConverterHelperImpl;
import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = DataConverterHelperImpl.class)
    private String houseNumber;

    @Column(nullable = false)
    @Convert(converter = DataConverterHelperImpl.class)
    private String street;

    @Column(nullable = false)
    @Convert(converter = DataConverterHelperImpl.class)
    private String neighborhood;

    @Column(nullable = false)
    @Convert(converter = DataConverterHelperImpl.class)
    private String city;

    @Column(nullable = false)
    @Convert(converter = DataConverterHelperImpl.class)
    private String state;

    @Convert(converter = DataConverterHelperImpl.class)
    private String cep;

    public Address() {
    }

    public Address(String houseNumber, String street, String neighborhood, String city, String state) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }

    public Address(String houseNumber, String street, String neighborhood, String city, String state, String cep) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.cep = cep;
    }

    public Address(Long id, String houseNumber, String street, String neighborhood, String city, String state, String cep) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
