package com.dev.customersbackend.common.mappers;

import com.dev.customersbackend.common.dtos.address.AddressRequestDTO;
import com.dev.customersbackend.common.dtos.address.AddressResponseDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.common.dtos.phone.PhoneResponseDTO;
import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.domain.entities.Address;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.entities.Phone;
import com.dev.customersbackend.domain.entities.Purchase;

public interface EntityMapper {
    // CUSTOMER METHODS
    Customer toModel(CustomerRequestDTO customerDTO);

    CustomerResponseDTO toDTO(Customer customer);
    // -----------------------------------------------

    // ADDRESS METHODS
    Address toModel(AddressRequestDTO addressDTO);

    AddressResponseDTO toDTO(Address address);
    // -----------------------------------------------

    // PHONE METHODS
    PhoneResponseDTO toDTO(Phone phone);
    // -----------------------------------------------

    // PURCHASE METHODS
    PurchaseResponseDTO toDTO(Purchase purchase);
    // -----------------------------------------------
}
