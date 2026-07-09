package net.myapplication.myapp.password.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.myapplication.myapp.password.repo.RefreshTokenRepo;
import net.myapplication.myapp.password.service.RefreshTokenSer;
import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;

@Service
public class RefreshTokenSerImpl implements RefreshTokenSer {
    @Value("${myapp.reset-password-token-expiration}")
    private String refreshExpiration;

    RefreshTokenRepo refreshTokenRepo;

    @Override
    public void deleteExpiredTokens() {
        refreshTokenRepo.deleteAllByExpiryDateBefore(
                LocalDateTime.now());

    }

    @Override
    public void revokeAllUserTokens(User user) {
        List<RefreshToken> tokens = refreshTokenRepo.findAllByUser(user);

        tokens.forEach(token -> token.setRevoked(true));

        refreshTokenRepo.saveAll(tokens);

    }

    @Override
    public void revokeToken(String token) {
        RefreshToken refreshToken = verifyToken(token);

        refreshToken.setRevoked(true);

        refreshTokenRepo.save(refreshToken);

    }

    @Override
    public RefreshToken saveRefreshToken(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .isRevoked(false)
                .deviceInfo("")
                .expiryDate(
                        LocalDateTime.now()
                                .plusSeconds(refreshExpiration))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

}
