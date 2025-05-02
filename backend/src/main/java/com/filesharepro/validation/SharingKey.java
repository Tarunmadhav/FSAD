package com.filesharepro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SharingKeyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SharingKey {
    String message() default "Invalid sharing key format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}