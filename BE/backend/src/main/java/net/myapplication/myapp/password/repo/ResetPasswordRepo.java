package net.myapplication.myapp.password.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import net.myapplication.myapp.password.entity.ResetPasswordToken;
import net.myapplication.myapp.user.entity.User;

public interface ResetPasswordRepo {
    Optional<ResetPasswordToken> findByToken(String token);

    Optional<ResetPasswordToken> findByUser(User user);

    void deleteAllByExpiryDateBefore(LocalDateTime time);
}
