package com.dev.customersbackend.domain.repositories;

import com.dev.customersbackend.domain.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByOrderByName(Pageable pageable);

    Page<Customer> findByNameContainingIgnoringCase(String name, Pageable pageable);

    Optional<Customer> findByPhoneNumber(String number);
}

