package net.myapplication.myapp.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.myapplication.myapp.enumpack.ERole;
import net.myapplication.myapp.user.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}

