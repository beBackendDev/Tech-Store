package net.myapplication.myapp.mail.service;

import net.myapplication.myapp.user.entity.User;

public interface MailSer {
    void sendVerificationEmail(
            User user,
            String verificationToken);

    void sendPasswordResetEmail(
            User user,
            String verificationToken);
}
