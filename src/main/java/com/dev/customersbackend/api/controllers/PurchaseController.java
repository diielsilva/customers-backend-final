package com.dev.customersbackend.api.controllers;

import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.domain.services.purchase.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(value = "/{customerId}")
    public ResponseEntity<PurchaseResponseDTO> save(@PathVariable long customerId) {
        PurchaseResponseDTO savedPurchase = purchaseService.save(customerId);
        return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<List<PurchaseResponseDTO>> findAllByCustomer(@PathVariable long customerId) {
        List<PurchaseResponseDTO> purchases = purchaseService.findAllByCustomer(customerId);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{customerId}/{purchaseId}")
    public ResponseEntity<Void> delete(@PathVariable long customerId, @PathVariable long purchaseId) {
        purchaseService.delete(customerId, purchaseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
