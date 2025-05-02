package com.filesharepro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileKeyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileKey {
    String message() default "Invalid file key format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}