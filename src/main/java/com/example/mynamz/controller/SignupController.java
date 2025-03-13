package com.example.mynamz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mynamz.model.dto.SignupRequest;
import com.example.mynamz.service.UserService;

@Controller
public class SignupController {
    
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        logger.info("Showing signup form");
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }
    
    @PostMapping("/signup")
    public String processSignup(@ModelAttribute SignupRequest signupRequest, 
                              RedirectAttributes redirectAttributes) {
        logger.info("Processing signup request for username: {}", signupRequest.getUsername());
        
        try {
            // 유효성 검사
            if (!signupRequest.isValid()) {
                logger.warn("Invalid signup request");
                redirectAttributes.addFlashAttribute("error", "모든 필수 항목을 올바르게 입력해주세요.");
                return "redirect:/signup";
            }
            
            // 비밀번호 일치 확인
            if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())) {
                logger.warn("Password mismatch");
                redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
                return "redirect:/signup";
            }
            
            // 회원가입 처리
            boolean success = userService.registerUser(signupRequest.toUser());
            
            if (success) {
                logger.info("Signup successful for username: {}", signupRequest.getUsername());
                redirectAttributes.addFlashAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
                return "redirect:/login";
            } else {
                logger.warn("Signup failed - user already exists");
                redirectAttributes.addFlashAttribute("error", "이미 존재하는 사용자입니다.");
                return "redirect:/signup";
            }
            
        } catch (Exception e) {
            logger.error("Error during signup", e);
            redirectAttributes.addFlashAttribute("error", "회원가입 처리 중 오류가 발생했습니다.");
            return "redirect:/signup";
        }
    }
}