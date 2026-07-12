package net.myapplication.myapp.password.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.password.entity.ResetPasswordToken;
import net.myapplication.myapp.user.entity.User;

public interface ResetPasswordRepo extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);

    Optional<ResetPasswordToken> findByUser(User user);

    void deleteAllByExpiryDateBefore(LocalDateTime time);
}
