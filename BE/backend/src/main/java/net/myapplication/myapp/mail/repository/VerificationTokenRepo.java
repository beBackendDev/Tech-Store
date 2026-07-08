package net.myapplication.myapp.mail.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.mail.entity.VerificationToken;
import net.myapplication.myapp.user.entity.User;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(
            String token
    );

    Optional<VerificationToken> findByUser(
            User user
    );

    boolean existsByUser(
            User user
    );

    void deleteByUser(
            User user
    );

    void deleteAllByExpiryDateBefore(
            LocalDateTime now
    );
    
}
