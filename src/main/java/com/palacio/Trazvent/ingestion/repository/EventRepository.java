package com.palacio.Trazvent.ingestion.repository;

import com.palacio.Trazvent.ingestion.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}