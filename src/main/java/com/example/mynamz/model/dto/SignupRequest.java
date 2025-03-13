package com.example.mynamz.model.dto;

import com.example.mynamz.model.entity.User;

public class SignupRequest {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String phone;
    
    // Default constructor
    public SignupRequest() {}
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPasswordConfirm() { return passwordConfirm; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               passwordConfirm != null && !passwordConfirm.trim().isEmpty() &&
               (email == null || email.matches("[A-Za-z0-9+_.-]+@(.+)$")) &&
               (phone == null || phone.matches("\\d{3}-\\d{4}-\\d{4}"));
    }
    
    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        return user;
    }
}