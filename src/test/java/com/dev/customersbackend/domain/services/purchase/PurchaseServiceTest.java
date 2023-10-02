package com.dev.customersbackend.domain.services.purchase;

import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import com.dev.customersbackend.domain.factories.CustomerFactory;
import com.dev.customersbackend.domain.repositories.CustomerRepository;
import com.dev.customersbackend.helpers.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PurchaseServiceTest {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = customerRepository.save(CustomerFactory.getVinicius());
    }

    @Test
    void save_AssertPurchaseWasSaved_WhenNoOneExceptionWasThrown() {
        PurchaseResponseDTO purchase = purchaseService.save(customer.getId());
        assertNotNull(purchase);
        assertNotNull(purchase.getId());
        assertNotNull(purchase.getDate());
        assertEquals(2, customer.getPurchases().size());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        long id = TestHelper.getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> purchaseService.save(id));
    }

    @Test
    void findAllByCustomer_AssertThatArePurchase_WhenCustomersIdIsFound() {
        List<PurchaseResponseDTO> purchases = purchaseService.findAllByCustomer(customer.getId());
        assertNotNull(purchases);
        assertFalse(purchases.isEmpty());
        assertEquals(1, purchases.size());
    }

    @Test
    void findAllByCustomer_AssertAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        long id = TestHelper.getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> purchaseService.findAllByCustomer(id));
    }

    @Test
    void delete_AssertPurchaseWasDeleted_WhenNoOneExceptionWasThrown() {
        long purchaseId = customer.getPurchases().get(0).getId();
        purchaseService.delete(customer.getId(), purchaseId);
        List<PurchaseResponseDTO> purchases = purchaseService.findAllByCustomer(customer.getId());
        assertNotNull(purchases);
        assertTrue(purchases.isEmpty());
    }

    @Test
    void delete_AssertAnExceptionWasThrown_WhenPurchaseIdIsNotFound() {
        long customerId = customer.getId();
        long purchaseId = TestHelper.getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> purchaseService.delete(customerId, purchaseId));
    }

}