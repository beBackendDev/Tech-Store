package net.myapplication.myapp.user.service;

import net.myapplication.myapp.user.entity.User;

public interface UserSer {
    boolean existByUsername(String username);
    boolean existByEmail(String email);
    void save(User user);
}
