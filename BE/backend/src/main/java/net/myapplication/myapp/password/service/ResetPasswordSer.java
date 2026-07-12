package net.myapplication.myapp.password.service;

import org.springframework.stereotype.Service;

import net.myapplication.myapp.password.entity.ResetPasswordToken;
import net.myapplication.myapp.user.entity.User;

@Service
public interface ResetPasswordSer {
    ResetPasswordToken createResetPasswordToken(
            User user);

    ResetPasswordToken verifyToken(
            String token);

    void deleteToken(
            ResetPasswordToken token);

    void deleteExpiredTokens();

    ResetPasswordToken save(
            ResetPasswordToken token);
}
