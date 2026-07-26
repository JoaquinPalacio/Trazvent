package com.palacio.Trazvent.analytics.dto;

import java.time.LocalDate;

public record DataPoint(
        LocalDate date,
        Integer value
) {}
