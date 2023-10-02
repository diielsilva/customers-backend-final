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
import org.springframework.stereotype.Component;

@Component
public class EntityMapperImpl implements EntityMapper {
    // CUSTOMER METHODS
    @Override
    public Customer toModel(CustomerRequestDTO customerDTO) {
        return new Customer(customerDTO.getName(), new Phone(customerDTO.getPhoneNumber()),
                customerDTO.getBirthDate(), toModel(customerDTO.getAddress()));
    }

    @Override
    public CustomerResponseDTO toDTO(Customer customer) {
        return new CustomerResponseDTO(customer.getId(), customer.getName(), toDTO(customer.getPhone()),
                toDTO(customer.getAddress()), customer.getBirthDate());
    }
    // -----------------------------------------------

    // ADDRESS METHODS
    @Override
    public Address toModel(AddressRequestDTO addressDTO) {
        return new Address(addressDTO.getHouseNumber(), addressDTO.getStreet(), addressDTO.getNeighborhood(),
                addressDTO.getCity(), addressDTO.getState(), addressDTO.getCep() == null ? "" : addressDTO.getCep());
    }

    @Override
    public AddressResponseDTO toDTO(Address address) {
        return new AddressResponseDTO(address.getId(), address.getHouseNumber(), address.getStreet(),
                address.getNeighborhood(), address.getCity(), address.getState(), address.getCep());
    }
    // -----------------------------------------------

    // PHONE METHODS
    @Override
    public PhoneResponseDTO toDTO(Phone phone) {
        return new PhoneResponseDTO(phone.getId(), phone.getNumber());
    }
    // -----------------------------------------------

    // PURCHASE METHODS
    @Override
    public PurchaseResponseDTO toDTO(Purchase purchase) {
        return new PurchaseResponseDTO(purchase.getId(), purchase.getDate());
    }
    // -----------------------------------------------
}
