package net.myapplication.myapp.mail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.user.entity.User;

@Service
@RequiredArgsConstructor

public class MailSerImpl
                implements MailSer {
        private final JavaMailSender mailSender;

        @Value("${spring.mail.username}")
        private String sender;

        @Value("${myapp.backend-url}")
        private String backendUrl;

        @Override
        public void sendVerificationEmail(
                        User user,
                        String verificationToken) {

                String verifyUrl = backendUrl
                                + "/api/auth/verify-email?token="
                                + verificationToken;

                SimpleMailMessage message = new SimpleMailMessage();

                message.setFrom(sender);

                message.setTo(user.getEmail());

                message.setSubject(
                                "Verify your account");

                message.setText(buildMailContent(
                                user,
                                verifyUrl));

                mailSender.send(message);

        }

        private String buildMailContent(
                        User user,
                        String verifyUrl) {

                return """
                                Hello %s,

                                Thank you for registering.

                                Please verify your email by visiting the following link:

                                %s

                                This link will expire in 24 hours.

                                If you did not create this account, please ignore this email.

                                Tech-Store Team Thanks you for choosing our service!
                                """
                                .formatted(
                                                user.getUsername(),
                                                verifyUrl);

        }

        @Override
        public void sendPasswordResetEmail(User user, String verificationToken) {
                String url = backendUrl
                                + "/api/auth/reset-password?token="
                                + verificationToken;

                SimpleMailMessage message = new SimpleMailMessage();

                message.setFrom(sender);

                message.setTo(
                                user.getEmail());

                message.setSubject(
                                "Reset Password");

                message.setText(
                                """
                                        Hello %s,

                                        We received a request to reset your password.

                                        Click the link below:

                                        %s

                                        This link expires in 15 minutes.

                                        If you didn't request this, ignore this email,
                                        Tech-Store Team Thanks you for choosing our service!

                                """
                                .formatted(
                                        user.getUsername(),
                                        url));

                mailSender.send(
                                message);
        }

}
