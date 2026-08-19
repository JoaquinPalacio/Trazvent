package com.palacio.Trazvent.auth.service;

import com.palacio.Trazvent.auth.dto.AuthenticationRequest;
import com.palacio.Trazvent.auth.dto.AuthenticationResponse;
import com.palacio.Trazvent.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final JwtService jwtService;
  // Spring Security nos provee esta clase que maneja la lógica de chequear la
  // contraseña internamente
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // 1. Esto le dice a Spring: "Ve a buscar el usuario con este email y comprueba
    // si esta contraseña (hasheada en BCrypt) coincide."
    // Si no coincide o no existe, lanza un BadCredentialsException automáticamente.
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()));

    // 2. Si pasó la línea anterior, el usuario es 100% legítimo. Lo buscamos en la
    // base de datos.
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow(); // Ya sabemos que existe, por lo que esto es seguro.

    // 3. (Opcional) Podemos inyectar información extra (Claims) dentro del token,
    // como su Rol o a qué Proyecto pertenece.
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("role", user.getRole().name());
    extraClaims.put("projectId", user.getProjectId().toString());

    // 4. Generamos el token usando el servicio que hicimos antes.
    var jwtToken = jwtService.generateToken(user.getEmail(), extraClaims);

    // 5. Devolvemos el JSON de respuesta.
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }
}
