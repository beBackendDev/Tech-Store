package net.myapplication.myapp.password.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.password.entity.ResetPasswordToken;
import net.myapplication.myapp.password.repo.ResetPasswordRepo;
import net.myapplication.myapp.password.service.ResetPasswordSer;
import net.myapplication.myapp.user.entity.User;

@Service
@RequiredArgsConstructor
public class ResetPasswordSerImpl implements ResetPasswordSer {

    private final ResetPasswordRepo resetPasswordRepo;

    @Override
    public ResetPasswordToken save(ResetPasswordToken token) {
        return resetPasswordRepo.save(token);
    }

    @Override
    @Transactional
    public ResetPasswordToken createResetPasswordToken(User user) {
        resetPasswordRepo
                .findByUser(user)
                .ifPresent(resetPasswordRepo::delete);

        ResetPasswordToken token = ResetPasswordToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .used(false)
                .expiryDate(
                        LocalDateTime.now()
                                .plusMinutes(15))
                .createdAt(
                        LocalDateTime.now())

                .build();

        return resetPasswordRepo.save(token);
    }

    @Override
    public ResetPasswordToken verifyToken(String token) {
        System.out.println("Received token: [" + token + "]");
        List<ResetPasswordToken> list = resetPasswordRepo.findAll();

        list.forEach(t -> System.out.println("DB TOKEN = [" + t.getToken() + "]"));
        ResetPasswordToken resetToken = resetPasswordRepo
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Reset token not found"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        return resetToken;
    }

    @Override
    public void deleteToken(ResetPasswordToken token) {
        resetPasswordRepo.delete(token);
    }

    @Override
    public void deleteExpiredTokens() {
        resetPasswordRepo.deleteAllByExpiryDateBefore(
                LocalDateTime.now());

    }

}
