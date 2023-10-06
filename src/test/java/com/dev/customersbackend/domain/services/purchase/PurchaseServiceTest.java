package com.dev.customersbackend.domain.services.purchase;

import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.common.mappers.EntityMapper;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.entities.Purchase;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import com.dev.customersbackend.domain.factories.PurchaseFactory;
import com.dev.customersbackend.domain.repositories.CustomerRepository;
import com.dev.customersbackend.helpers.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.dev.customersbackend.domain.factories.CustomerFactory.getViniciusWithId;
import static com.dev.customersbackend.domain.factories.PurchaseFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.when;

@ExtendWith(value = SpringExtension.class)
class PurchaseServiceTest {
    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Mock
    private CustomerRepository repository;

    @Mock
    private EntityMapper mapper;

    private Customer customer;

    @BeforeEach
    void setup() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(getViniciusWithId()));

        when(mapper.toDTO(ArgumentMatchers.any(Purchase.class))).thenReturn(getPurchaseResponseDTO());
    }

    @Test
    void save_AssertPurchaseWasSaved_WhenNoOneExceptionWasThrown() {
        PurchaseResponseDTO purchase = purchaseService.save(1L);
        assertNotNull(purchase);
        assertEquals(1L, purchase.getId());
        assertNotNull(purchase.getDate());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> purchaseService.save(1L));
    }

    @Test
    void findAllByCustomer_AssertThatArePurchase_WhenCustomersIdIsFound() {
        List<PurchaseResponseDTO> purchases = purchaseService.findAllByCustomer(1L);
        assertNotNull(purchases);
        assertFalse(purchases.isEmpty());
        assertEquals(1, purchases.size());
    }

    @Test
    void findAllByCustomer_AssertAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> purchaseService.findAllByCustomer(1L));
    }

    @Test
    void delete_AssertPurchaseWasDeleted_WhenNoOneExceptionWasThrown() {
        assertDoesNotThrow(() -> purchaseService.delete(1L, 1L));
    }

    @Test
    void delete_AssertAnExceptionWasThrown_WhenPurchaseIdIsNotFound() {
        long purchaseId = TestHelper.getInvalidId();
        assertThrows(EntityNotFoundException.class, () -> purchaseService.delete(1L, purchaseId));
    }

}