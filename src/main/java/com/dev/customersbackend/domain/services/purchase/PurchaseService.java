package com.dev.customersbackend.domain.services.purchase;

import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseResponseDTO save(long customerId);

    List<PurchaseResponseDTO> findAllByCustomer(long customerId);

    void delete(long customerId, long purchaseId);
}
