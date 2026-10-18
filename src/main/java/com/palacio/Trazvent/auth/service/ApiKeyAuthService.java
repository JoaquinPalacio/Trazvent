package com.palacio.Trazvent.auth.service;

import com.palacio.Trazvent.common.exception.ApiKeyError;
import com.palacio.Trazvent.common.exception.InvalidApiKeyException;
import com.palacio.Trazvent.project.entity.Project;
import com.palacio.Trazvent.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.UUID;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ApiKeyAuthService {
    public static record ApiKey(String apiKeyHashed, String rawApiKey) {}
    private final ProjectRepository projectRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public UUID validateAndGetProjectId(String rawApiKey) {
        if (rawApiKey == null || rawApiKey.isBlank()) {
            throw new InvalidApiKeyException(ApiKeyError.MISSING_KEY);
        }

        String hashedKey = hashApiKey(rawApiKey);

        Project project = projectRepository.findByApiKeyHash(hashedKey)
                .orElseThrow(() -> new InvalidApiKeyException(ApiKeyError.INVALID_KEY));

        return project.getId();
    }

    public ApiKey createApiKey() {
        byte[] rawBytes = new byte[32];
        secureRandom.nextBytes(rawBytes);
        String rawApiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(rawBytes);
        String apiKeyHashed = hashApiKey(rawApiKey);

        return new ApiKey(apiKeyHashed, rawApiKey);
    }

    private String hashApiKey(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error encrypting API Key", e);
        }
    }

}
