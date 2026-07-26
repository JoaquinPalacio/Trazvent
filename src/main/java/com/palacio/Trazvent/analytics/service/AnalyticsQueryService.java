package com.palacio.Trazvent.analytics.service;

import com.palacio.Trazvent.analytics.dto.DataPoint;
import com.palacio.Trazvent.analytics.dto.QueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsQueryService {

    private final JdbcTemplate jdbcTemplate;

    public List<DataPoint> executeQuery(UUID projectId, QueryRequest request) {

        String sql = """
            SELECT date, SUM(count) as total
            FROM aggregates
            WHERE project_id = ?
              AND event_name = ?
              AND date >= ?
              AND date <= ?
            GROUP BY date
            ORDER BY date ASC
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new DataPoint(
                        rs.getObject("date", LocalDate.class),
                        rs.getInt("total")
                ),
                projectId,
                request.eventName(),
                request.startDate(),
                request.endDate()
        );
    }
}
