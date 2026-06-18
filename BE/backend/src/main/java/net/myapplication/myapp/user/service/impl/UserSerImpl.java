package net.myapplication.myapp.user.service.impl;

import org.springframework.stereotype.Service;

import net.myapplication.myapp.user.dto.UserDTO;
import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.repository.UserRepo;
import net.myapplication.myapp.user.service.UserSer;

@Service
public class UserSerImpl implements UserSer {
    private final UserRepo userRepository;

    public UserSerImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existByUsername(String username) {
            return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
    
}
