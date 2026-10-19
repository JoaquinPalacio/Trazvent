package com.palacio.Trazvent.auth.service;

import com.palacio.Trazvent.auth.dto.AuthenticationRequest;
import com.palacio.Trazvent.auth.dto.AuthenticationResponse;
import com.palacio.Trazvent.auth.dto.RegisterRequest;
import com.palacio.Trazvent.auth.dto.RegisterResponse;
import com.palacio.Trazvent.auth.repository.UserRepository;
import com.palacio.Trazvent.auth.entity.User;
import com.palacio.Trazvent.auth.entity.Role;
import com.palacio.Trazvent.project.entity.Project;
import com.palacio.Trazvent.project.repository.ProjectRepository;
import com.palacio.Trazvent.common.exception.InvalidRegisterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ApiKeyAuthService apiKeyService;
    private final AuthenticationManager authenticationManager;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("projectId", user.getProjectId().toString());

        var jwtToken = jwtService.generateToken(user.getEmail(), extraClaims);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRegisterException("The provided email already exists.");
        }

        ApiKeyAuthService.ApiKey apiKey = apiKeyService.createApiKey();
        String apiKeyHashed = apiKey.apiKeyHashed();
        String rawApiKey = apiKey.rawApiKey();

        UUID projectId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        Project project = Project.builder()
                .id(projectId)
                .name(request.getProject())
                .apiKeyHash(apiKeyHashed)
                .createdAt(now)
                .build();
        projectRepository.save(project);

        UUID userId = UUID.randomUUID();
        String passwordHashed = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .id(userId)
                .projectId(projectId)
                .email(email)
                .passwordHash(passwordHashed)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("projectId", projectId.toString());

        var jwtToken = jwtService.generateToken(email, extraClaims);

        return RegisterResponse.builder()
                .token(jwtToken)
                .apiKey(rawApiKey)
                .email(email)
                .build();
    }
}
