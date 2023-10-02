package com.dev.customersbackend.common.mappers;

import com.dev.customersbackend.common.dtos.address.AddressResponseDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.common.dtos.phone.PhoneResponseDTO;
import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.domain.entities.Address;
import com.dev.customersbackend.domain.factories.AddressFactory;
import com.dev.customersbackend.domain.factories.CustomerFactory;
import com.dev.customersbackend.domain.factories.PhoneFactory;
import com.dev.customersbackend.domain.factories.PurchaseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(value = "local")
class EntityMapperTest {
    @Autowired
    private EntityMapper mapper;

    @Test
    void toDTO_AssertThatCustomerWasSuccessfullyMapped() {
        CustomerResponseDTO customer = mapper.toDTO(CustomerFactory.getViniciusWithId());
        assertEquals(1L, customer.getId());
        assertEquals("Vinicius Tiago Nathan Pires", customer.getName());
        assertEquals(1L, customer.getPhone().getId());
        assertEquals(PhoneFactory.getViniciusPhoneWithId().getNumber(), customer.getPhone().getNumber());
        assertEquals(CustomerFactory.getViniciusWithId().getBirthDate(), customer.getBirthDate());
        assertEquals(1L, customer.getAddress().getId());
        assertEquals("680", customer.getAddress().getHouseNumber());
        assertEquals("Travessa Amauri Furquim", customer.getAddress().getStreet());
        assertEquals("Parque Novo Século", customer.getAddress().getNeighborhood());
        assertEquals("Campo Grande", customer.getAddress().getCity());
        assertEquals("MS", customer.getAddress().getState());
        assertEquals("79072538", customer.getAddress().getCep());
    }

    @Test
    void toModel_AssertThatAddressWasSuccessfullyMapped() {
        Address address = mapper.toModel(AddressFactory.getViniciusAddressRequestDTO());
        assertEquals("680", address.getHouseNumber());
        assertEquals("Travessa Amauri Furquim", address.getStreet());
        assertEquals("Parque Novo Século", address.getNeighborhood());
        assertEquals("Campo Grande", address.getCity());
        assertEquals("MS", address.getState());
        assertEquals("79072538", address.getCep());
    }

    @Test
    void toDTO_AssertThatAddressWasSuccessfullyMapped() {
        AddressResponseDTO address = mapper.toDTO(AddressFactory.getViniciusAddressWithId());
        assertEquals(1L, address.getId());
        assertEquals("680", address.getHouseNumber());
        assertEquals("Travessa Amauri Furquim", address.getStreet());
        assertEquals("Parque Novo Século", address.getNeighborhood());
        assertEquals("Campo Grande", address.getCity());
        assertEquals("MS", address.getState());
        assertEquals("79072538", address.getCep());
    }

    @Test
    void toDTO_AssertThatPhoneWasSuccessfullyMapped() {
        PhoneResponseDTO phone = mapper.toDTO(PhoneFactory.getViniciusPhoneWithId());
        assertEquals(1L, phone.getId());
        assertEquals("67999797354", phone.getNumber());
    }

    @Test
    void toDTO_AssertThatPurchaseWasSuccessfullyMapped() {
        PurchaseResponseDTO purchase = mapper.toDTO(PurchaseFactory.getPurchase());
        assertEquals(1L, purchase.getId());
        assertEquals(purchase.getDate(), PurchaseFactory.getPurchase().getDate());
    }
}