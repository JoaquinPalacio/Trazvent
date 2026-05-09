package com.palacio.Trazvent.ingestion.controller;

import com.palacio.Trazvent.ingestion.dto.EventRequest;
import com.palacio.Trazvent.ingestion.service.IngestionService;
import com.palacio.Trazvent.auth.service.ApiKeyAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final IngestionService ingestionService;
    private final ApiKeyAuthService apiKeyAuthService;

    @PostMapping
    public ResponseEntity<Map<String, String>> ingestEvent(
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Valid @RequestBody EventRequest request) {

        UUID projectId = apiKeyAuthService.validateAndGetProjectId(apiKey);

        UUID eventId = ingestionService.processEvent(request, projectId);

        return ResponseEntity.accepted().body(Map.of(
                "eventId", eventId.toString(),
                "message", "Event recorded."
        ));
    }
}