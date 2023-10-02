package com.dev.customersbackend.domain.factories;

import com.dev.customersbackend.domain.entities.Phone;

public class PhoneFactory {
    private PhoneFactory() {
    }

    public static Phone getViniciusPhoneWithId() {
        return new Phone(1L, "67999797354");
    }

    public static Phone getViniciusPhoneWithoutId() {
        return new Phone("67999797354");
    }

    public static Phone getAgathaPhoneWithId() {
        return new Phone(2L, "71998904074");
    }

    public static Phone getAgathaPhoneWithoutId() {
        return new Phone("71998904074");
    }
}
