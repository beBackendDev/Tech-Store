package net.myapplication.myapp.user.refreshtoken.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor

public class RefreshTokenRequest {
    private String refreshToken;
}
