package net.myapplication.myapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SignInRequestDto  {
    @NotBlank(message = "Email is required")
    private String email;    
    
    @NotBlank(message = "Password is required")
    private String password;   
}
