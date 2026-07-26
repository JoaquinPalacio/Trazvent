package com.palacio.Trazvent.analytics.controller;

import com.palacio.Trazvent.analytics.dto.DataPoint;
import com.palacio.Trazvent.analytics.dto.QueryRequest;
import com.palacio.Trazvent.analytics.service.AnalyticsQueryService;
import com.palacio.Trazvent.auth.service.ApiKeyAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsQueryController {

    private final ApiKeyAuthService apiKeyAuthService;
    private final AnalyticsQueryService analyticsQueryService;

    @PostMapping("/query")
    public ResponseEntity<List<DataPoint>> queryMetrics(
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Valid @RequestBody QueryRequest request) {

        UUID projectId = apiKeyAuthService.validateAndGetProjectId(apiKey);

        List<DataPoint> response = analyticsQueryService.executeQuery(projectId, request);

        return ResponseEntity.ok(response);
    }
}
