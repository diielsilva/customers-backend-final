package com.dev.customersbackend.common.security.helpers.converter;

import com.dev.customersbackend.common.security.helpers.encryption.DataEncryptionHelper;
import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

@Component
public class DataConverterHelperImpl implements AttributeConverter<String, String> {
    private final DataEncryptionHelper helper;

    public DataConverterHelperImpl(DataEncryptionHelper helper) {
        this.helper = helper;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return helper.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return helper.decrypt(dbData);
    }
}
