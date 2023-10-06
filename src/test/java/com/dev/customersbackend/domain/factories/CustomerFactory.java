package com.dev.customersbackend.domain.factories;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.entities.Purchase;

import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerFactory {

    private CustomerFactory() {
    }

    public static Customer getVinicius() {
        Customer customer = new Customer("Vinicius Tiago Nathan Pires", PhoneFactory.getViniciusPhoneWithoutId(), LocalDate.now(),
                AddressFactory.getViniciusAddressWithoutId(),
                new ArrayList<>());
        customer.getPurchases().add(new Purchase(customer, LocalDate.now()));
        return customer;
    }

    public static Customer getViniciusWithId() {
        Customer customer = new Customer(1L, "Vinicius Tiago Nathan Pires", PhoneFactory.getViniciusPhoneWithId(), LocalDate.now(),
                AddressFactory.getViniciusAddressWithId(),
                new ArrayList<>());
        customer.getPurchases().add(new Purchase(customer, LocalDate.now()));
        return customer;
    }

    public static Customer getAgatha() {
        Customer customer = new Customer("Agatha Hadassa Sebastiana Silva", PhoneFactory.getAgathaPhoneWithoutId(), LocalDate.now(),
                AddressFactory.getViniciusAddressWithoutId(),
                new ArrayList<>());
        customer.getPurchases().add(new Purchase(customer, LocalDate.now()));
        customer.getPurchases().add(new Purchase(customer, LocalDate.now()));
        return customer;
    }

    public static Customer getAgathaWithId() {
        Customer customer = new Customer(2L, "Agatha Hadassa Sebastiana Silva", PhoneFactory.getAgathaPhoneWithoutId(), LocalDate.now(),
                AddressFactory.getViniciusAddressWithoutId(),
                new ArrayList<>());
        customer.getPurchases().add(new Purchase(customer, LocalDate.now()));
        customer.getPurchases().add(new Purchase(customer, LocalDate.now()));
        return customer;
    }

    public static Customer getEmptyCustomer() {
        return new Customer();
    }

    public static CustomerRequestDTO getViniciusRequestDTO() {
        return new CustomerRequestDTO("Vinicius Tiago Nathan Pires", PhoneFactory.getViniciusPhoneWithoutId().getNumber(), AddressFactory.getViniciusAddressRequestDTO(),
                LocalDate.now().minusYears(23));
    }

    public static CustomerRequestDTO getAgathaRequestDTO() {
        return new CustomerRequestDTO("Agatha Hadassa Sebastiana Silva", PhoneFactory.getAgathaPhoneWithoutId().getNumber(), AddressFactory.getAgathaAddressRequestDTO(),
                LocalDate.now().minusYears(25));
    }

    public static CustomerRequestDTO getEmptyRequestDTO() {
        return new CustomerRequestDTO();
    }

    public static CustomerResponseDTO getViniciusResponseDTO() {
        return new CustomerResponseDTO(1L, "Vinicius Tiago Nathan Pires", PhoneFactory.getViniciusPhoneResponseDTO(), AddressFactory.getViniciusAddressResponseDTO(),
                LocalDate.now().minusYears(23));
    }

    public static CustomerResponseDTO getAgathaResponseDTO() {
        return new CustomerResponseDTO(2L,"Agatha Hadassa Sebastiana Silva", PhoneFactory.getAgathaPhoneResponseDTO(), AddressFactory.getAgathaAddressResponseDTO(),
                LocalDate.now().minusYears(25));
    }
}
