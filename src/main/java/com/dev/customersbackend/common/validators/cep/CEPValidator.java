package com.dev.customersbackend.common.validators.cep;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CEPValidator implements ConstraintValidator<CEP, String> {
    @Override
    public void initialize(CEP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && !value.isBlank()) {
            return value.length() == 8 && areAllCharsNumbers(value) && !areAllCharsEquals(value);
        }
        return true;
    }

    private boolean areAllCharsNumbers(String cep) {
        for (int index = 0; index < cep.length(); index++) {
            boolean isANumber = Character.isDigit(cep.charAt(index));
            if (!isANumber) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllCharsEquals(String cep) {
        int sequence = 0;
        char firstLetter = cep.charAt(0);
        for (int index = 0; index < cep.length(); index++) {
            if (cep.charAt(index) == firstLetter) {
                sequence++;
            }
        }
        return sequence == cep.length();
    }
}
