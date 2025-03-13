package com.example.mynamz.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mynamz.service.EmailService;
import com.example.mynamz.service.UserService;

@Controller
@RequestMapping("/")
public class FindAccountController {
    
    private static final Logger logger = LoggerFactory.getLogger(FindAccountController.class);
    
    private final UserService userService;
    private final EmailService emailService;

    public FindAccountController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/find-id")
    public String findIdForm() {
        return "auth/find-id";
    }

    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "auth/find-password";
    }

    @PostMapping("/find-id-proc")
    public String findIdProc(@RequestParam("email") String email, 
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            logger.info("아이디 찾기 요청 - 이메일: {}", email);
            String foundId = userService.findIdByEmail(email);

            if (foundId != null) {
                String maskedId = maskUsername(foundId);
                model.addAttribute("foundId", maskedId);
                model.addAttribute("email", email);
                return "auth/find-id-result";
            } else {
                redirectAttributes.addFlashAttribute("error", "해당 이메일로 등록된 아이디가 없습니다.");
                return "redirect:/auth/find-id";
            }
        } catch (Exception e) {
            logger.error("아이디 찾기 실패", e);
            redirectAttributes.addFlashAttribute("error", "아이디 찾기 처리 중 오류가 발생했습니다.");
            return "redirect:/auth/find-id";
        }
    }

    @PostMapping("/find-password-proc")
    public String findPasswordProc(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {
        try {
            logger.info("비밀번호 찾기 요청 - 아이디: {}, 이메일: {}", username, email);

            if (userService.validateUsernameAndEmail(username, email)) {
                String tempPassword = userService.generateTempPassword();
                userService.updatePassword(username, tempPassword);
                
                if (emailService.sendTemporaryPassword(email, tempPassword)) {
                    redirectAttributes.addFlashAttribute("success", "임시 비밀번호가 이메일로 발송되었습니다.");
                    return "redirect:/login";
                } else {
                    redirectAttributes.addFlashAttribute("error", "이메일 발송에 실패했습니다.");
                    return "redirect:/auth/find-password";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "입력하신 정보와 일치하는 회원이 없습니다.");
                return "redirect:/auth/find-password";
            }
        } catch (Exception e) {
            logger.error("비밀번호 찾기 실패", e);
            redirectAttributes.addFlashAttribute("error", "비밀번호 찾기 처리 중 오류가 발생했습니다.");
            return "redirect:/auth/find-password";
        }
    }

    // 아이디 마스킹 처리 메서드
    private String maskUsername(String username) {
        if (username == null || username.length() <= 2) {
            return username;
        }
        // 앞 2자리는 보여주고 나머지는 *로 마스킹
        return username.substring(0, 2) + "*".repeat(username.length() - 2);
    }
}