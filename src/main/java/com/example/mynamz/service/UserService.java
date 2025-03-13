package com.example.mynamz.service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mynamz.model.entity.User;
import com.example.mynamz.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 메소드
    @Transactional
    public boolean registerUser(User user) {
        try {
            // 이미 존재하는 username인지 확인
            if (userRepository.findByUsername(user.getUsername()) != null) {
                logger.warn("이미 존재하는 사용자명: {}", user.getUsername());
                return false;
            }
            
            // 이미 존재하는 이메일인지 확인
            if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()) != null) {
                logger.warn("이미 존재하는 이메일: {}", user.getEmail());
                return false;
            }
            
            // 비밀번호 암호화
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // 초기 설정
            user.setLoginAttempts(0);
            user.setLocked(false);
            
            // QR 코드 생성 (랜덤 문자열)
            String qrCode = generateQRCode();
            user.setQrCode(qrCode);
            
            // 사용자 저장
            userRepository.save(user);
            logger.info("새로운 사용자 등록 완료: {}", user.getUsername());
            return true;
            
        } catch (Exception e) {
            logger.error("사용자 등록 중 오류 발생", e);
            return false;
        }
    }

    // QR 코드 생성 메소드
    private String generateQRCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder qrCode = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 10; i++) {
            qrCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return qrCode.toString();
    }

    public String findIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
        }
        return user.getUsername();
    }

    public boolean validateUsernameAndEmail(String username, String email) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getEmail().equals(email)) {
            return true;
        }
        return false;
    }

    public String generateTempPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // 최소 요구사항 충족
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));

        // 나머지 5자리 랜덤 생성 (총 8자리)
        String allChars = upperCase + lowerCase + numbers;
        for (int i = 0; i < 5; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        String shuffled = shuffleString(password.toString());
        logger.info("임시 비밀번호 생성 완료");
        return shuffled;
    }

    private String shuffleString(String input) {
        List<Character> characters = input.chars()
            .mapToObj(ch -> (char)ch)
            .collect(Collectors.toList());
        Collections.shuffle(characters);
        return characters.stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    @Transactional
    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("비밀번호 업데이트 완료: {}", username);
    }
}