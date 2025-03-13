package com.example.mynamz.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mynamz.model.entity.PasswordResetToken;
import com.example.mynamz.model.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
    Optional<PasswordResetToken> findByUser(User user);
}