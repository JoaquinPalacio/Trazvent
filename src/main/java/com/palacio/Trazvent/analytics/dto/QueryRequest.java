package com.palacio.Trazvent.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

@ValidProperties
public record QueryRequest(
        @NotBlank(message = "The 'eventName' field is mandatory and cannot be empty.") String eventName,

        @NotNull(message = "The 'startDate' field is mandatory and cannot be empty.") LocalDate startDate,

        @NotNull(message = "The 'endDate' field is mandatory and cannot be empty.") LocalDate endDate,

        @NotNull(message = "The 'metric' field is mandatory and cannot be empty.") Operation metric,

        Map<String, Object> filters,

        String property) {
}
