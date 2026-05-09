package com.palacio.Trazvent.ingestion.service;

import com.palacio.Trazvent.ingestion.dto.EventRequest;
import com.palacio.Trazvent.ingestion.entity.Event;
import com.palacio.Trazvent.ingestion.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final EventRepository eventRepository;

    @Transactional
    public UUID processEvent(EventRequest request, UUID projectId) {
        UUID eventId = UUID.randomUUID();

        Event event = Event.builder()
                .id(eventId)
                .projectId(projectId)
                .eventName(request.event())
                .timestamp(request.timestamp())
                .userId(request.userId())
                .anonymousId(request.anonymousId())
                .properties(request.properties())
                .build();

        eventRepository.save(event);
        return eventId;
    }
}