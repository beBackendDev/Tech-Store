package net.myapplication.myapp.mail.entity;

import java.time.LocalDateTime;

import net.myapplication.myapp.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verify_token", columnList = "token"),
        @Index(name = "idx_verify_expiry", columnList = "expiry_date")
})
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * UUID
     */
    @Column(nullable = false, unique = true, length = 120)
    private String token;

    /**
     * Mỗi User chỉ có một VerificationToken còn hiệu lực
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Đã sử dụng hay chưa
     */
    @Column(nullable = false)
    private boolean verified;
}
