package net.myapplication.myapp.password.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;

public interface RefreshTokenRepo {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUser(User user);

    void deleteAllByExpiryDateBefore(LocalDateTime time);

    void saveAll(List<RefreshToken> tokens);
}
