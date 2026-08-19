package net.myapplication.myapp.user.refreshtoken.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;
import net.myapplication.myapp.user.refreshtoken.repository.RefreshTokenRepo;

@Service
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
                        String token) {

                RefreshToken refreshToken = RefreshToken.builder()
                                .token(token)
                                .user(user)
                                .isRevoked(false)
                                .deviceInfo("web")
                                .expiryDate(
                                                LocalDateTime.now()
                                                                .plusSeconds(
                                                                                refreshExpiration))
                                .build();
                refreshTokenRepo.save(
                                refreshToken);
                return refreshToken;
        }

        @Override
        public RefreshToken verifyToken(String token) {
                RefreshToken refreshToken = refreshTokenRepo
                                .findByToken(token)
                                .orElseThrow(
                                                () -> new RuntimeException(
                                                                "Refresh token not found"));
                if (refreshToken.isRevoked()) {
                        throw new RuntimeException("Refresh token revoked");
                }
                if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                        throw new RuntimeException("Refresh token expired, please try again.");
                }

                return refreshToken;
        }

        @Override
        public void revokeToken(String token) {
                // RefreshToken refreshToken = verifyToken(token);
                refreshTokenRepo.findByToken(token)
                                .ifPresent(refreshToken -> {
                                        refreshToken.setRevoked(true);

                                        refreshTokenRepo.save(
                                                        refreshToken);
                                });

        }

        @Override
        public void revokeAllUserTokens(User user) {
                List<RefreshToken> tokens = refreshTokenRepo.findAllByUser(user);

                tokens.forEach(token -> token.setRevoked(true));

                refreshTokenRepo.saveAll(tokens);
        }

        @Override
        public void deleteExpiredTokens() {
                refreshTokenRepo.deleteAllByExpiryDateBefore(
                                LocalDateTime.now());
        }
}
