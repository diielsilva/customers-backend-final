package com.dev.customersbackend.domain.factories;

import com.dev.customersbackend.domain.entities.Purchase;

import java.time.LocalDate;

public class PurchaseFactory {

    private PurchaseFactory() {
    }

    public static Purchase getPurchase() {
        return new Purchase(1L, null, LocalDate.now());
    }
}
