package com.palacio.Trazvent.analytics.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PropertiesValidator.class)
@Documented
public @interface ValidProperties {
    String message() default "The 'property' field is mandatory and cannot be empty for AVG or SUM operations.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
