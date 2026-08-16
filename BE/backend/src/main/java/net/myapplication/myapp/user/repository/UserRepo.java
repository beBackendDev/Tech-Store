package net.myapplication.myapp.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.myapplication.myapp.user.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    //Xưr lý vấn đề LazyInitializationException khi getRoles() trong UserDetailsImpl
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
