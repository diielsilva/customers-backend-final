package com.dev.customersbackend.domain.services.purchase;

import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.common.mappers.EntityMapper;
import com.dev.customersbackend.domain.entities.Customer;
import com.dev.customersbackend.domain.entities.Purchase;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import com.dev.customersbackend.domain.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final CustomerRepository customerRepository;
    private final EntityMapper entityMapper;

    public PurchaseServiceImpl(CustomerRepository customerRepository, EntityMapper entityMapper) {
        this.customerRepository = customerRepository;
        this.entityMapper = entityMapper;
    }

    @Transactional
    @Override
    public PurchaseResponseDTO save(long customerId) {
        Customer customer = findCustomerByIdOrThrowsAnException(customerId);
        Purchase purchase = new Purchase(customer, LocalDate.now());
        customer.getPurchases().add(purchase);
        customerRepository.save(customer);
        return entityMapper.toDTO(purchase);
    }

    @Override
    public List<PurchaseResponseDTO> findAllByCustomer(long customerId) {
        Customer customer = findCustomerByIdOrThrowsAnException(customerId);
        return customer.getPurchases().stream().map(entityMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public void delete(long customerId, long purchaseId) {
        Customer customer = findCustomerByIdOrThrowsAnException(customerId);
        Purchase purchase = findPurchaseById(customer.getPurchases(), purchaseId);
        if (purchase == null) {
            throw new EntityNotFoundException("A compra: " + purchaseId + " não foi encontrada.");
        }
        customer.getPurchases().remove(purchase);
        customerRepository.save(customer);
    }

    private Purchase findPurchaseById(List<Purchase> purchases, long purchaseId) {
        for (Purchase purchase : purchases) {
            if (purchase.getId() == purchaseId) {
                return purchase;
            }
        }
        return null;
    }

    private Customer findCustomerByIdOrThrowsAnException(long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("O cliente: " + customerId + " não foi encontrado."));
    }
}
