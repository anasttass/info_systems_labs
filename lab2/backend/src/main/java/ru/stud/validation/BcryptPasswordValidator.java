package ru.stud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

public class BcryptPasswordValidator implements ConstraintValidator<ValidBcryptPassword, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null/blank values are handled by @NotBlank.
        return value == null || value.getBytes(StandardCharsets.UTF_8).length <= 72;
    }
}
