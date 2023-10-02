package com.dev.customersbackend.domain.services.customer;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponseDTO save(CustomerRequestDTO dto);

    Page<CustomerResponseDTO> findAllOrderedByName(Pageable pageable);

    Page<CustomerResponseDTO> findByName(String name, Pageable pageable);

    CustomerResponseDTO findById(long id);

    CustomerResponseDTO update(long id, CustomerRequestDTO dto);

    void delete(long id);
}
