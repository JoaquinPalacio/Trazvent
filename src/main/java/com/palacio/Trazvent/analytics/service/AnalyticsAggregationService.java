package com.palacio.Trazvent.analytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsAggregationService {

    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 15000)
    @Transactional
    public void aggregateEvents() {
        log.info("Starting batch aggregation process...");

        String upsertSql = """
            INSERT INTO aggregates (project_id, event_name, date, count)
            SELECT project_id, event_name, CAST(timestamp AS DATE), COUNT(*)
            FROM events
            WHERE processed = FALSE
            GROUP BY project_id, event_name, CAST(timestamp AS DATE)
            ON CONFLICT (project_id, event_name, date)
            DO UPDATE SET count = aggregates.count + EXCLUDED.count;
        """;

        int updatedRows = jdbcTemplate.update(upsertSql);

        if (updatedRows > 0) {
            jdbcTemplate.update("UPDATE events SET processed = TRUE WHERE processed = FALSE");
            log.info("Aggregation complete. New events were processed.");
        } else {
            log.info("There are no new events to process.");
        }
    }
}