package net.myapplication.myapp.user.refreshtoken.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.refreshtoken.entity.RefreshToken;

@Repository
public interface RefreshTokenRepo
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}
