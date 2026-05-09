package com.palacio.Trazvent.project.repository;

import com.palacio.Trazvent.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByApiKeyHash(String apiKeyHash);
}