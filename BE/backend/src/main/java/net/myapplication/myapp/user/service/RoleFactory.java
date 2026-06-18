package net.myapplication.myapp.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.myapplication.myapp.enumpack.ERole;
import net.myapplication.myapp.exception.RoleNotFoundException;
import net.myapplication.myapp.user.entity.Role;
import net.myapplication.myapp.user.repository.RoleRepo;

@Component
public class RoleFactory {
    @Autowired
    RoleRepo roleRepository;

    public Role getInstance(String role) throws RoleNotFoundException {
        switch (role) {
            case "admin" -> {
                return roleRepository.findByName(ERole
                    .ADMIN);
            }
            case "user" -> {
                return roleRepository.findByName(ERole.USER);
            }
            default -> throw new RoleNotFoundException("Role not found for " +  role);
        }
    }
}

