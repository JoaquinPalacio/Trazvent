package com.palacio.Trazvent.analytics.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.palacio.Trazvent.analytics.dto.DataPoint;
import com.palacio.Trazvent.analytics.dto.QueryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsQueryService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public List<DataPoint> executeQuery(UUID projectId, QueryRequest request) {

        if (request.filters() == null || request.filters().isEmpty()) {
            return queryAggregatesTable(projectId, request);
        } else {
            return queryRawEventsTable(projectId, request);
        }
    }

    private List<DataPoint> queryAggregatesTable(UUID projectId, QueryRequest request) {
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

    private List<DataPoint> queryRawEventsTable(UUID projectId, QueryRequest request) {
        StringBuilder sql = new StringBuilder("""
            SELECT CAST(timestamp AS DATE) as date, COUNT(*) as total
            FROM events
            WHERE project_id = ?
              AND event_name = ?
              AND CAST(timestamp AS DATE) >= ?
              AND CAST(timestamp AS DATE) <= ?
        """);

        List<Object> parameters = new ArrayList<>(List.of(
                projectId,
                request.eventName(),
                request.startDate(),
                request.endDate()
        ));

        try {
            String jsonFilters = objectMapper.writeValueAsString(request.filters());
            sql.append(" AND properties @> ?::jsonb ");
            parameters.add(jsonFilters);
            
        } catch (JacksonException e) {
            throw new RuntimeException("Error processing dynamic filters", e);
        }

        sql.append(" GROUP BY CAST(timestamp AS DATE) ORDER BY date ASC");

        return jdbcTemplate.query(
                sql.toString(),
                (rs, rowNum) -> new DataPoint(
                        rs.getObject("date", LocalDate.class),
                        rs.getInt("total")
                ),
                parameters.toArray()
        );
    }
}
