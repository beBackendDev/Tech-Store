package net.myapplication.myapp.user.refreshtoken.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
}
