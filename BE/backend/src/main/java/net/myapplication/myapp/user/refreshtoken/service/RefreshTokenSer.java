package net.myapplication.myapp.user.refreshtoken.service;

import org.springframework.stereotype.Service;

import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;

@Service
public interface RefreshTokenSer {
    RefreshToken saveRefreshToken(
            User user,
            String token
    );

    RefreshToken verifyToken(
            String token
    );

    void revokeToken(
            String token
    );
}
