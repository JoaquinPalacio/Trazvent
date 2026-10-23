package com.palacio.Trazvent.analytics.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PropertiesValidator implements ConstraintValidator<ValidProperties, QueryRequest> {

    @Override
    public boolean isValid(QueryRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }
        if ((request.metric() == Operation.AVG || request.metric() == Operation.SUM)
                && (request.property() == null || request.property().isEmpty())) {
            return false;
        }
        return true;
    }
}
