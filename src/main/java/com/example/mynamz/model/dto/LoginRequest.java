package com.example.mynamz.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;

    // 유효성 검사 메소드
    public boolean isValid() {
        return loginId != null && !loginId.trim().isEmpty() 
            && password != null && !password.trim().isEmpty();
    }

    // 전화번호 형식 검증
    public boolean isPhoneNumber() {
        return loginId != null && loginId.matches("\\d{3}-\\d{4}-\\d{4}");
    }

    // 이메일 형식 검증
    public boolean isEmail() {
        return loginId != null && loginId.matches("[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Getter 메서드들
    public String getLoginId() {
        return this.loginId;
    }

    public String getPassword() {
        return this.password;
    }

    // Setter 메서드들
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}