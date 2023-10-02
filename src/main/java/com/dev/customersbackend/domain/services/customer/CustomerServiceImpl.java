package com.dev.customersbackend.domain.services.customer;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.common.mappers.EntityMapper;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.exceptions.ConstraintException;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import com.dev.customersbackend.domain.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final EntityMapper entityMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, EntityMapper entityMapper) {
        this.customerRepository = customerRepository;
        this.entityMapper = entityMapper;
    }

    @Transactional
    @Override
    public CustomerResponseDTO save(CustomerRequestDTO customerDTO) {
        Customer customer = entityMapper.toModel(customerDTO);
        if (isPhoneInUse(customer.getPhone().getNumber())) {
            throw new ConstraintException("O telefone: " + customer.getPhone().getNumber() + " já está em uso.");
        }
        customerRepository.save(customer);
        return entityMapper.toDTO(customer);
    }

    @Override
    public Page<CustomerResponseDTO> findAllOrderedByName(Pageable pageable) {
        Page<Customer> customers = customerRepository.findByOrderByName(pageable);
        return customers.map(entityMapper::toDTO);
    }

    @Override
    public Page<CustomerResponseDTO> findByName(String name, Pageable pageable) {
        Page<Customer> customers = customerRepository.findByNameContaining(name, pageable);
        return customers.map(entityMapper::toDTO);
    }

    @Override
    public CustomerResponseDTO findById(long id) {
        Customer customer = findByIdOrThrowsException(id);
        return entityMapper.toDTO(customer);
    }

    @Transactional
    @Override
    public CustomerResponseDTO update(long id, CustomerRequestDTO customerDTO) {
        Customer customer = findByIdOrThrowsException(id);
        Customer updatedCustomer = entityMapper.toModel(customerDTO);
        if (isPhoneInUse(customerDTO.getPhoneNumber()) && !customer.getPhone().getNumber().equals(customerDTO.getPhoneNumber())) {
            throw new ConstraintException("O telefone: " + customer.getPhone().getNumber() + " já está em uso.");
        }
        customer.setName(updatedCustomer.getName());
        customer.getPhone().setNumber(updatedCustomer.getPhone().getNumber());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setBirthDate(updatedCustomer.getBirthDate());
        customerRepository.save(customer);
        return entityMapper.toDTO(customer);
    }

    @Transactional
    @Override
    public void delete(long id) {
        Customer model = findByIdOrThrowsException(id);
        customerRepository.delete(model);
    }

    private boolean isPhoneInUse(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    private Customer findByIdOrThrowsException(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("O cliente: " + id + " não foi encontrado."));
    }
}
