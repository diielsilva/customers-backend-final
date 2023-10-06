package com.dev.customersbackend.domain.services.customer;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.common.mappers.EntityMapper;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.exceptions.ConstraintException;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import com.dev.customersbackend.domain.factories.PhoneFactory;
import com.dev.customersbackend.domain.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dev.customersbackend.domain.factories.CustomerFactory.*;
import static com.dev.customersbackend.helpers.TestHelper.getInvalidId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.when;

@ExtendWith(value = SpringExtension.class)
class CustomerServiceTest {
    @InjectMocks
    private CustomerServiceImpl service;

    @Mock
    private CustomerRepository repository;

    @Mock
    private EntityMapper mapper;

    @BeforeEach
    void setup() {
        when(repository.save(ArgumentMatchers.any(Customer.class))).thenReturn(getViniciusWithId());
        when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        when(repository.findByOrderByName(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(getAgathaWithId(), getViniciusWithId())));
        when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(repository.findByNameContainingIgnoringCase(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(List.of(getViniciusWithId())));

        when(mapper.toModel(ArgumentMatchers.any(CustomerRequestDTO.class))).thenReturn(getVinicius());
        when(mapper.toDTO(ArgumentMatchers.any(Customer.class))).thenReturn(getViniciusResponseDTO());
    }

    @Test
    void save_AssertCustomerWasSave_WhenNoOneExceptionWasThrown() {
        CustomerResponseDTO customer = service.save(getViniciusRequestDTO());
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
        when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(Optional.of(getViniciusWithId()));
        CustomerRequestDTO customer = getViniciusRequestDTO();
        assertThrows(ConstraintException.class, () -> service.save(customer));
    }

    @Test
    void findAllOrderedByName_AssertThatAreCustomers_WhenHaveStoredCustomers() {
        when(repository.findByOrderByName(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(getAgathaWithId())));
        when(mapper.toDTO(ArgumentMatchers.any(Customer.class))).thenReturn(getAgathaResponseDTO());
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertEquals("Agatha Hadassa Sebastiana Silva", customers.getContent().get(0).getName());
        assertEquals(1, customers.getContent().size());
    }

    @Test
    void findAllOrderedByName_AssertThatAreNotCustomers_WhenDoNotHaveStoredCustomers() {
        when(repository.findByOrderByName(ArgumentMatchers.any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        Page<CustomerResponseDTO> customers = service.findAllOrderedByName(PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findByName_AssertThatAreCustomers_WhenCustomersNameIsFound() {
        when(repository.findByNameContainingIgnoringCase(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(List.of(getAgathaWithId())));
        when(mapper.toDTO(ArgumentMatchers.any(Customer.class))).thenReturn(getAgathaResponseDTO());
        Page<CustomerResponseDTO> customers = service.findByName("aga", PageRequest.of(0, 2));
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
        assertEquals(1, customers.getContent().size());
        assertEquals("Agatha Hadassa Sebastiana Silva", customers.getContent().get(0).getName());
    }

    @Test
    void findByName_AssertThatAreNotCustomers_WhenCustomersNameIsNotFound() {
        when(repository.findByNameContainingIgnoringCase(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(List.of()));
        Page<CustomerResponseDTO> customers = service.findByName("xzxc", PageRequest.of(0, 2));
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findById_AssertThatAreCustomers_WhenCustomersIdIsFound() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getVinicius()));
        CustomerResponseDTO responseDTO = service.findById(1L);
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Vinicius Tiago Nathan Pires", responseDTO.getName());
    }

    @Test
    void findById_AssertThatAnExceptionWasThrows_WhenCustomersIdIsNotFound() {
        long id = getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void update_AssertCustomerWasUpdated_WhenNoOneExceptionWasThrown() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getViniciusWithId()));
        LocalDate birthDate = LocalDate.now().minusYears(20);
        CustomerResponseDTO responseDTO = getViniciusResponseDTO();
        responseDTO.setName("Vinicius");
        responseDTO.getPhone().setNumber("12345");
        responseDTO.getAddress().setCity("Vitória de St. Antão");
        responseDTO.setBirthDate(birthDate);
        when(mapper.toDTO(ArgumentMatchers.any(Customer.class))).thenReturn(responseDTO);
        CustomerResponseDTO updatedCustomer = service.update(1L, getViniciusRequestDTO());
        assertNotNull(updatedCustomer);
        assertEquals("Vinicius", updatedCustomer.getName());
        assertEquals("12345", updatedCustomer.getPhone().getNumber());
        assertEquals("Vitória de St. Antão", updatedCustomer.getAddress().getCity());
        assertEquals(birthDate, updatedCustomer.getBirthDate());
    }

    @Test
    void update_AssertCustomerWasUpdated_WhenCustomersPhoneIsInUseAndEqualsToTheCurrentCustomer() {
        CustomerResponseDTO responseDTO = getViniciusResponseDTO();
        responseDTO.setName("Vinicius");
        responseDTO.getAddress().setCity("Vitória de St. Antão");
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getViniciusWithId()));
        when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(Optional.of(getViniciusWithId()));
        when(mapper.toDTO(ArgumentMatchers.any(Customer.class))).thenReturn(responseDTO);
        CustomerResponseDTO updatedCustomer = service.update(1L, getViniciusRequestDTO());
        assertNotNull(updatedCustomer);
        assertNotNull(updatedCustomer);
        assertEquals("Vinicius", updatedCustomer.getName());
        assertEquals("Vitória de St. Antão", updatedCustomer.getAddress().getCity());
    }

    @Test
    void update_AssertThatAnExceptionWasThrown_WhenCustomersPhoneIsInUseAndIsDifferentToTheCurrentCustomer() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getViniciusWithId()));
        when(repository.findByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(Optional.of(getViniciusWithId()));
        when(mapper.toModel(ArgumentMatchers.any(CustomerRequestDTO.class))).thenReturn(getAgathaWithId());
        CustomerRequestDTO updatedCustomer = getViniciusRequestDTO();
        assertThrows(ConstraintException.class, () -> service.update(1L, updatedCustomer));
    }

    @Test
    void update_AssertThatAnExceptionWasThrows_WhenCustomerIdIsNotFound() {
        CustomerRequestDTO customer = getViniciusRequestDTO();
        assertThrows(EntityNotFoundException.class, () -> service.update(1L, customer));
    }

    @Test
    void delete_AssertCustomerWasDelete_WhenCustomersIdIsFound() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getViniciusWithId()));
        assertDoesNotThrow(() -> service.delete(1L));
    }

    @Test
    void delete_AssertThatAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
    }
}