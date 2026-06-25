package net.myapplication.myapp.user.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponseDto {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
