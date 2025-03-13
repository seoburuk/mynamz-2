package com.example.mynamz.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mynamz.model.entity.InvalidTokenException;
import com.example.mynamz.model.entity.PasswordResetToken;
import com.example.mynamz.model.entity.ResourceNotFoundException;
import com.example.mynamz.model.entity.User;
import com.example.mynamz.repository.PasswordResetTokenRepository;
import com.example.mynamz.repository.UserRepository;

@Service
@Transactional
public class AccountRecoveryService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountRecoveryService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String findIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("해당 이메일로 등록된 사용자가 없습니다.");
        }
        return maskUsername(user.getUsername());
    }

    // 아이디 마스킹 처리 메소드 추가
    private String maskUsername(String username) {
        if (username == null || username.length() <= 2) {
            return username;
        }
        // 앞 2자리는 보여주고 나머지는 *로 마스킹
        return username.substring(0, 2) + "*".repeat(username.length() - 2);
    }

    @Transactional
    public boolean sendPasswordResetEmail(String username, String email) {
        try {
            Optional<User> userOptional = userRepository.findByUsernameAndEmail(username, email);
            if (!userOptional.isPresent()) {
                throw new ResourceNotFoundException("일치하는 사용자 정보가 없습니다.");
            }
            User user = userOptional.get();
            
            // 기존 토큰이 있다면 삭제
            tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
            
            String token = generateResetToken();
            
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
            
            tokenRepository.save(resetToken);
            
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            String emailContent = createPasswordResetEmailContent(resetLink);
            
            return emailService.sendEmail(user.getEmail(), "비밀번호 재설정 안내", emailContent);
            
        } catch (Exception e) {
            logger.error("비밀번호 재설정 이메일 발송 중 오류 발생", e);
            return false;
        }
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private String createPasswordResetEmailContent(String resetLink) {
        return String.format("""
            안녕하세요.
            
            비밀번호 재설정을 위한 링크가 생성되었습니다.
            아래 링크를 클릭하여 비밀번호를 재설정해 주세요.
            
            %s
            
            이 링크는 24시간 동안 유효합니다.
            본인이 요청하지 않은 경우 이 이메일을 무시하셔도 됩니다.
            
            감사합니다.""", 
            resetLink);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        try {
            Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
            if (!tokenOptional.isPresent()) {
                throw new InvalidTokenException("유효하지 않은 토큰입니다.");
            }
            
            PasswordResetToken resetToken = tokenOptional.get();
            if (resetToken.isExpired()) {
                tokenRepository.delete(resetToken);
                throw new InvalidTokenException("만료된 토큰입니다.");
            }
            
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            tokenRepository.delete(resetToken);
            logger.info("비밀번호 재설정 완료: {}", user.getUsername());
            
            return true;
        } catch (Exception e) {
            logger.error("비밀번호 재설정 중 오류 발생", e);
            return false;
        }
    }
}