package com.palacio.Trazvent.ingestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Map;

public record EventRequest(
        @NotBlank(message = "The 'event' field is mandatory and cannot be empty.")
        String event,

        @NotNull(message = "The 'timestamp' field is required.")
        OffsetDateTime timestamp,

        String userId,

        String anonymousId,

        @NotNull(message = "The 'properties' field is mandatory, even if it is empty.")
        Map<String, Object> properties
) {}