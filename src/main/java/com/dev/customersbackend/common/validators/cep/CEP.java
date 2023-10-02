package com.dev.customersbackend.common.validators.cep;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Constraint(validatedBy = CEPValidator.class)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CEP {
    String message() default "O CEP é inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
