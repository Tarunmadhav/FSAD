package com.filesharepro.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class SharingKeyValidator implements ConstraintValidator<SharingKey, String> {
    private static final Pattern SHARING_KEY_PATTERN = Pattern.compile("^[A-Z0-9]{6}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }
        return SHARING_KEY_PATTERN.matcher(value).matches();
    }
}