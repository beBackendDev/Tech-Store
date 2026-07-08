package net.myapplication.myapp.mail.service;

import org.springframework.mail.javamail.JavaMailSender;

import net.myapplication.myapp.mail.entity.VerificationToken;
import net.myapplication.myapp.user.entity.User;

public interface MailSer {
    void sendVerificationEmail(
            User user,
            String verificationToken);

    VerificationToken createVerificationToken(
            User user);

    VerificationToken verifyToken(
            String token);

    VerificationToken resendVerificationToken(
            User user);

    void deleteExpiredTokens();
}
