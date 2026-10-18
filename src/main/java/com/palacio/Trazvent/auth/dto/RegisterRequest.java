package com.palacio.Trazvent.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email field cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password field cannot be empty")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#_\\-.+])[A-Za-z\\d@$!%*?&#_\\-.+]{8,}$", message = "Password must have one uppercase letter, one lowercase letter, one number and one special character (@$!%*?&#_-.+)")
    private String password;

    @NotBlank(message = "Project field cannot be empty")
    private String project;
}
