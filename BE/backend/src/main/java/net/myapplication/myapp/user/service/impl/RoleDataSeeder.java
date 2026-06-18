package net.myapplication.myapp.user.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.myapplication.myapp.enumpack.ERole;
import net.myapplication.myapp.user.entity.Role;
import net.myapplication.myapp.user.repository.RoleRepo;

@Component
public class RoleDataSeeder {
    @Autowired
    private RoleRepo roleRepository;

    @EventListener
    @Transactional
    public void LoadRoles(ContextRefreshedEvent event) {

        List<ERole> roles = Arrays.stream(ERole.values()).toList();

        for(ERole erole: roles) {
            if (roleRepository.findByName(erole)==null) {
                roleRepository.save(new Role(erole));
            }
        }

    }
}

