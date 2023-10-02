package com.dev.customersbackend.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @Column(nullable = false)
    private LocalDate date;

    public Purchase() {
    }

    public Purchase(Customer customer, LocalDate date) {
        this.customer = customer;
        this.date = date;
    }

    public Purchase(Long id, Customer customer, LocalDate date) {
        this.id = id;
        this.customer = customer;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
