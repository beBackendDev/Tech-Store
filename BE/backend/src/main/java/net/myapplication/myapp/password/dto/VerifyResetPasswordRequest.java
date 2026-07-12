package net.myapplication.myapp.password.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class VerifyResetPasswordRequest {
    @NotBlank
    private String token;
}
