package net.myapplication.myapp.password.service;

import org.springframework.stereotype.Service;

import net.myapplication.myapp.password.entity.ResetPasswordToken;

@Service
public interface ResetPasswordSer {
    ResetPasswordToken save(
            ResetPasswordToken token);
}
