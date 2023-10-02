package com.dev.customersbackend.api.controllers;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.domain.services.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(@RequestBody @Valid CustomerRequestDTO customerDTO) {
        CustomerResponseDTO savedCustomer = customerService.save(customerDTO);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> findAllOrderedByName(Pageable pageable) {
        Page<CustomerResponseDTO> customers = customerService.findAllOrderedByName(pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerResponseDTO> findById(@PathVariable long id) {
        CustomerResponseDTO customer = customerService.findById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Page<CustomerResponseDTO>> findByName(@RequestParam String name, Pageable pageable) {
        Page<CustomerResponseDTO> customers = customerService.findByName(name, pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable long id, @RequestBody @Valid CustomerRequestDTO customerDTO) {
        CustomerResponseDTO updatedCustomer = customerService.update(id, customerDTO);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        customerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
