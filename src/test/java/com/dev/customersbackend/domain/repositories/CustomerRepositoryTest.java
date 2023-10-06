package com.dev.customersbackend.domain.repositories;

import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.helpers.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.dev.customersbackend.domain.factories.CustomerFactory.*;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles(value = "local")
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repository;


    @Test
    void save_AssertCustomerWasSaved_WhenNoOneExceptionIsThrown() {
        Customer customer = repository.save(getVinicius());
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenCustomersPhoneIsInUse() {
        repository.save(getVinicius());
        Customer customer = getVinicius();
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(customer));
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenAnEmptyCustomerIsGiven() {
        Customer customer = getEmptyCustomer();
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(customer));
    }

    @Test
    void findByOrderByName_AssertThatAreCustomers_WhenHaveStoredCustomers() {
        repository.saveAll(List.of(getVinicius(), getAgatha()));
        Page<Customer> customers = repository.findByOrderByName(PageRequest.of(0, 2));
        assertFalse(customers.isEmpty());
        assertEquals(2, customers.getContent().size());
        assertEquals("Agatha Hadassa Sebastiana Silva", customers.getContent().get(0).getName());
        assertEquals("Vinicius Tiago Nathan Pires", customers.getContent().get(1).getName());
        assertNotNull(customers.getContent().get(0).getPhone());
        assertNotNull(customers.getContent().get(0).getAddress());
        assertNotNull(customers.getContent().get(1).getPhone());
        assertNotNull(customers.getContent().get(1).getAddress());
        assertEquals(2, customers.getContent().get(0).getPurchases().size());
        assertEquals(1, customers.getContent().get(1).getPurchases().size());
    }

    @Test
    void findByOrderByName_AssertThatAreNotCustomers_WhenDoNotHaveStoredCustomers() {
        Page<Customer> customers = repository.findByOrderByName(PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findByNameContaining_AssertThatAreCustomers_WhenCustomersNameIsFound() {
        repository.saveAll(List.of(getVinicius(), getAgatha()));
        Page<Customer> customers = repository.findByNameContainingIgnoringCase("vini", PageRequest.of(0, 2));
        assertEquals(1, customers.getContent().size());
        assertEquals("Vinicius Tiago Nathan Pires", customers.getContent().get(0).getName());
    }

    @Test
    void findByNameContaining_AssertThatAreNotCustomers_WhenCustomersNameIsNotFound() {
        repository.saveAll(List.of(getVinicius(), getAgatha()));
        Page<Customer> customers = repository.findByNameContainingIgnoringCase("alan", PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findByPhoneNumber_AssertThatAreCustomer_WhenCustomersPhoneIsInUse() {
        Customer customer = repository.save(getVinicius());
        Optional<Customer> optional = repository.findByPhoneNumber(customer.getPhone().getNumber());
        assertNotNull(optional);
        assertTrue(optional.isPresent());
    }

    @Test
    void findByPhoneNumber_AssertThatAreCustomer_WhenCustomersPhoneIsNotFound() {
        repository.save(getVinicius());
        Customer customer = getAgatha();
        Optional<Customer> optional = repository.findByPhoneNumber(customer.getPhone().getNumber());
        assertNotNull(optional);
        assertTrue(optional.isEmpty());
    }

    @Test
    void findById_AssertThatAreCustomer_WhenCustomersIdIsFound() {
        Customer customer = repository.save(getVinicius());
        Optional<Customer> optional = repository.findById(customer.getId());
        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(optional.get().getId(), customer.getId());
    }

    @Test
    void findById_AssertThatAreNoCustomer_WhenCustomersIdIsNotFound() {
        Optional<Customer> optional = repository.findById(TestHelper.getInvalidId());
        assertNotNull(optional);
        assertTrue(optional.isEmpty());
    }

    @Test
    void update_AssertThatCustomerWasUpdated_WhenNoOneExceptionIsThrown() {
        Customer customer = repository.save(getVinicius());
        String name = customer.getName();
        customer.setName("Fulano");
        repository.save(customer);
        List<Customer> customers = repository.findAll();
        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertNotNull(customers.get(0).getName(), name);
    }

    @Test
    void delete_AssertThatCustomerWasDeleted_WhenCustomersIdIsFound() {
        Customer customer = repository.save(getVinicius());
        repository.delete(customer);
        List<Customer> customers = repository.findAll();
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }
}