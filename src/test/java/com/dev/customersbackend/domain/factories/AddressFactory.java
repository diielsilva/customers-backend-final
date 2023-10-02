package com.dev.customersbackend.domain.factories;

import com.dev.customersbackend.common.dtos.address.AddressRequestDTO;
import com.dev.customersbackend.domain.entities.Address;

public class AddressFactory {
    private AddressFactory() {
    }

    public static Address getViniciusAddressWithId() {
        return new Address(1L, "680", "Travessa Amauri Furquim", "Parque Novo Século", "Campo Grande", "MS", "79072538");
    }

    public static Address getViniciusAddressWithoutId() {
        return new Address("680", "Travessa Amauri Furquim", "Parque Novo Século", "Campo Grande", "MS", "79072538");
    }

    public static Address getAgathaAddressWithId() {
        return new Address(1L, "136", "2ª Travessa Valnice Batista", "Campinas de Pirajá", "Salvador", "BA", "41275216");
    }

    public static Address getAgathaAddressWithoutId() {
        return new Address("136", "2ª Travessa Valnice Batista", "Campinas de Pirajá", "Salvador", "BA", "41275216");
    }

    public static AddressRequestDTO getViniciusAddressRequestDTO() {
        return new AddressRequestDTO("680", "Travessa Amauri Furquim", "Parque Novo Século", "Campo Grande", "MS", "79072538");
    }

    public static AddressRequestDTO getAgathaAddressRequestDTO() {
        return new AddressRequestDTO("136", "2ª Travessa Valnice Batista", "Campinas de Pirajá", "Salvador", "BA", "41275216");
    }

}
