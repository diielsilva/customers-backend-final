package com.dev.customersbackend.domain.services.customer;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.domain.exceptions.ConstraintException;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import com.dev.customersbackend.domain.factories.CustomerFactory;
import com.dev.customersbackend.domain.factories.PhoneFactory;
import com.dev.customersbackend.helpers.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerServiceTest {
    @Autowired
    private CustomerService service;

    @Test
    void save_AssertCustomerWasSaved_WhenNoOneExceptionWasThrown() {
        CustomerResponseDTO customer = service.save(CustomerFactory.getViniciusRequestDTO());
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertNotNull(customer.getPhone());
        assertNotNull(customer.getAddress());
        assertNotNull(customer.getBirthDate());
        assertEquals("Vinicius Tiago Nathan Pires", customer.getName());
        assertEquals(PhoneFactory.getViniciusPhoneWithId().getNumber(), customer.getPhone().getNumber());
        assertEquals("680", customer.getAddress().getHouseNumber());
        assertEquals("Travessa Amauri Furquim", customer.getAddress().getStreet());
        assertEquals("Parque Novo Século", customer.getAddress().getNeighborhood());
        assertEquals("Campo Grande", customer.getAddress().getCity());
        assertEquals("MS", customer.getAddress().getState());
        assertEquals("79072538", customer.getAddress().getCep());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenCustomersPhoneIsInUse() {
        service.save(CustomerFactory.getViniciusRequestDTO());
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        assertThrows(ConstraintException.class, () -> service.save(customer));
    }

    @Test
    void findAllOrderedByName_AssertThatAreCustomers_WhenHaveStoredCustomers() {
        service.save(CustomerFactory.getViniciusRequestDTO());
        service.save(CustomerFactory.getAgathaRequestDTO());
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertEquals("Agatha Hadassa Sebastiana Silva", customers.getContent().get(0).getName());
        assertEquals("Vinicius Tiago Nathan Pires", customers.getContent().get(1).getName());
    }

    @Test
    void findAllOrderedByName_AssertThatAreNotCustomers_WhenDoNotHaveStoredCustomers() {
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findByName_AssertThatAreCustomers_WhenCustomersNameIsFound() {
        service.save(CustomerFactory.getAgathaRequestDTO());
        service.save(CustomerFactory.getViniciusRequestDTO());
        Page<CustomerResponseDTO> customers = service.findByName("aga", PageRequest.of(0, 2));
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
        assertEquals(1, customers.getContent().size());
        assertEquals("Agatha Hadassa Sebastiana Silva", customers.getContent().get(0).getName());
    }

    @Test
    void findByName_AssertThatAreNotCustomers_WhenCustomersNameIsNotFound() {
        service.save(CustomerFactory.getAgathaRequestDTO());
        service.save(CustomerFactory.getViniciusRequestDTO());
        Page<CustomerResponseDTO> customers = service.findByName("xzxc", PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findById_AssertThatAreCustomers_WhenCustomersIdIsFound() {
        CustomerResponseDTO dto = service.save(CustomerFactory.getViniciusRequestDTO());
        CustomerResponseDTO responseDto = service.findById(dto.getId());
        assertNotNull(responseDto);
        assertEquals(responseDto.getId(), dto.getId());
    }

    @Test
    void findById_AssertThatAnExceptionWasThrows_WhenCustomersIdIsNotFound() {
        long id = TestHelper.getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void update_AssertCustomerWasUpdated_WhenNoOneExceptionWasThrown() {
        LocalDate birthDate = LocalDate.now();
        CustomerResponseDTO customer = service.save(CustomerFactory.getViniciusRequestDTO());
        CustomerRequestDTO updatedCustomer = CustomerFactory.getViniciusRequestDTO();
        updatedCustomer.setName("Vinicius");
        updatedCustomer.setPhoneNumber("12345");
        updatedCustomer.getAddress().setCity("Vitória de St. Antão");
        updatedCustomer.setBirthDate(birthDate);
        service.update(customer.getId(), updatedCustomer);
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertNotNull(customers);
        assertEquals(1, customers.getContent().size());
        assertEquals("Vinicius", customers.getContent().get(0).getName());
        assertEquals("12345", customers.getContent().get(0).getPhone().getNumber());
        assertEquals("Vitória de St. Antão", customers.getContent().get(0).getAddress().getCity());
        assertEquals(birthDate, customers.getContent().get(0).getBirthDate());
    }

    @Test
    void update_AssertCustomerWasUpdated_WhenCustomersPhoneIsInUseAndEqualsToTheCurrentCustomer() {
        CustomerResponseDTO customer = service.save(CustomerFactory.getViniciusRequestDTO());
        CustomerRequestDTO updateCustomer = CustomerFactory.getViniciusRequestDTO();
        updateCustomer.setName("Vinicius");
        service.update(customer.getId(), updateCustomer);
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertNotNull(customers);
        assertEquals(1, customers.getContent().size());
        assertEquals("Vinicius", customers.getContent().get(0).getName());
    }

    @Test
    void update_AssertThatAnExceptionWasThrown_WhenCustomersPhoneIsInUseAndIsDifferentToTheCurrentCustomer() {
        CustomerResponseDTO firstCustomer = service.save(CustomerFactory.getViniciusRequestDTO());
        long id = firstCustomer.getId();
        CustomerResponseDTO secondCustomer = service.save(CustomerFactory.getAgathaRequestDTO());
        CustomerRequestDTO updatedCustomer = CustomerFactory.getViniciusRequestDTO();
        updatedCustomer.setPhoneNumber(secondCustomer.getPhone().getNumber());
        assertThrows(ConstraintException.class, () -> service.update(id, updatedCustomer));
    }

    @Test
    void update_AssertThatAnExceptionWasThrows_WhenCustomerIdIsNotFound() {
        long id = TestHelper.getInvalidId();
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        assertThrows(EntityNotFoundException.class, () -> service.update(id, customer));
    }

    @Test
    void delete_AssertCustomerWasDeleted_WhenCustomersIdIsFound() {
        CustomerResponseDTO customer = service.save(CustomerFactory.getViniciusRequestDTO());
        service.delete(customer.getId());
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void delete_AssertThatAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        long id = TestHelper.getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> service.delete(id));
    }
}