import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.mail.entity.VerificationToken;
import net.myapplication.myapp.mail.repository.VerificationTokenRepo;
import net.myapplication.myapp.user.entity.User;

@Service
@RequiredArgsConstructor
@Transactional
public class MailSerImpl implements MailSer {
    private final VerificationTokenRepo verificationTokenRepo;

    @Value("${myapp.verification-token-expiration}")
    private long expiration;

    @Override
    public VerificationToken createVerificationToken(User user) {
        verificationTokenRepo
                .findByUser(user)
                .ifPresent(
                        verificationTokenRepo::delete);

        VerificationToken token = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .verified(false)
                .expiryDate(
                        LocalDateTime.now()
                                .plusSeconds(expiration))
                .build();

        return verificationTokenRepo.save(
                token);
    }

    @Override
    public void deleteExpiredTokens() {
        verificationTokenRepo
                .deleteAllByExpiryDateBefore(
                        LocalDateTime.now());
    }

    }

    @Override
    public VerificationToken resendVerificationToken(User user) {
        verificationTokenRepo
                .findByUser(user)
                .ifPresent(
                        verificationTokenRepo::delete);

        return createVerificationToken(
                user);
    }

    @Override
    public void sendVerificationEmail(User user, String verificationToken) {
        // TODO Auto-generated method stub

    }

    @Override
    public VerificationToken verifyToken(String token) {

        VerificationToken verificationToken = verificationTokenRepo
                .findByToken(token)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Verification token not found."));

        if (verificationToken.isVerified()) {
            throw new RuntimeException(
                    "Verification token already used.");
        }

        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Verification token expired.");
        }

        verificationToken.setVerified(true);

        User user = verificationToken.getUser();

        user.setEnabled(true);

        return verificationToken;
    }

}
