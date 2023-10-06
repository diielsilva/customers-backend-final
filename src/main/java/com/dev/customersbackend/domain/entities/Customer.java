package com.dev.customersbackend.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Phone phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    public Customer() {
    }

    public Customer(String name, Phone phone, LocalDate birthDate, Address address) {
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
    }

    public Customer(String name, Phone phone, LocalDate birthDate, Address address, List<Purchase> purchases) {
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.purchases = purchases;
    }

    public Customer(Long id, String name, Phone phone, LocalDate birthDate, Address address, List<Purchase> purchases) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.purchases = purchases;
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

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }
}
