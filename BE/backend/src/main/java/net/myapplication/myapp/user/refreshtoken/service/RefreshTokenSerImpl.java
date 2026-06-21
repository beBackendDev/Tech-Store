package net.myapplication.myapp.user.refreshtoken.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;
import net.myapplication.myapp.user.refreshtoken.repository.RefreshTokenRepo;

@Component
public class RefreshTokenSerImpl implements RefreshTokenSer {
    private final RefreshTokenRepo refreshTokenRepo;

    @Value("${myapp.jwtRefreshTokenExpiration}")
    private long refreshExpiration;

    public RefreshTokenSerImpl(RefreshTokenRepo refreshTokenRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Override
    public RefreshToken saveRefreshToken(
            User user,
            String token
    ) {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(token)
                        .user(user)
                        .isRevoked(false)
                        .deviceInfo("")
                        .expiryDate(
                                LocalDateTime.now()
                                        .plusSeconds(
                                                refreshExpiration
                                        )
                        )
                        .build();

        return refreshTokenRepo.save(
                refreshToken
        );
    }

    @Override
    public RefreshToken verifyToken(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyToken'");
    }

    @Override
    public void revokeToken(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'revokeToken'");
    }
}
