package com.palacio.Trazvent.analytics.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

public record DataPoint(
        LocalDate date,
        BigDecimal value) {
}
