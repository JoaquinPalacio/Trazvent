
package com.palacio.Trazvent.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  @NotBlank(message = "Email field cannot be empty")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password field cannot be empty")
  private String password;

}
