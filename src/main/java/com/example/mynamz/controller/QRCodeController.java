package com.example.mynamz.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mynamz.model.dto.BusinessCardDTO;
import com.example.mynamz.model.entity.BusinessCard;
import com.example.mynamz.model.entity.User;
import com.example.mynamz.model.entity.UserQRCode;
import com.example.mynamz.repository.BusinessCardRepository;
import com.example.mynamz.repository.UserQRCodeRepository;
import com.example.mynamz.repository.UserRepository;
import com.example.mynamz.service.BusinessCardService;

@Controller
public class QRCodeController {
    
    @Value("${app.domain}") 
    private String domain;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BusinessCardService businessCardService;
    
    @Autowired
    private BusinessCardRepository businessCardRepository;
    
    @Autowired
    private UserQRCodeRepository userQRCodeRepository;

    // QR코드로 명함 정보 조회 (공개 접근 가능)
    // /manage 경로 제거
    @GetMapping("/c/{identifier}")
    public String viewCardByQr(@PathVariable String identifier, Model model) {
        try {
            UserQRCode userQRCode = userQRCodeRepository.findByQrCodeIdentifier(identifier)
                .orElseThrow(() -> new RuntimeException("잘못된 QR 코드입니다."));
            
            User cardOwner = userQRCode.getUser();
            List<BusinessCard> ownerCards = businessCardRepository.findMyCards(cardOwner);
            
            // 디버깅을 위한 로그 추가
            System.out.println("Found cards: " + ownerCards.size());
            ownerCards.forEach(card -> {
                System.out.println("Card: " + card.getName() + ", isOwner: " + card.isOwner());
            });
            
            List<BusinessCardDTO> cardDTOs = ownerCards.stream()
                .filter(BusinessCard::isOwner)
                .map(BusinessCardDTO::fromEntity)
                .collect(Collectors.toList());
                
            // DTO 변환 후 확인
            System.out.println("Converted DTOs: " + cardDTOs.size());
            
            model.addAttribute("cards", cardDTOs);
            model.addAttribute("isLoggedIn", true);  // 로그인 상태 명시적 설정
            model.addAttribute("ownerName", cardOwner.getUsername());
            
            return "card/view-qr";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "명함을 불러오는 중 오류가 발생했습니다.");
            return "error/custom-error";
        }
    }

    // QR 코드로 명함 저장
    @PostMapping("/c/{identifier}/save") 
    public String saveCardToMyAccount(@PathVariable String identifier,
                                    @RequestParam Long cardId,
                                    RedirectAttributes redirectAttributes) {
        
        // 현재 로그인한 사용자 확인
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equals("anonymousUser")) {
            // 로그인 페이지로 리다이렉트하면서 현재 URL과 cardId를 파라미터로 전달
            return "redirect:/login?redirect=/c/" + identifier + "&cardId=" + cardId;
        }

        try {
            User currentUser = userRepository.findByUsername(auth.getName());
            
            // 명함 조회
            BusinessCard originalCard = businessCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
            
            // 자신의 명함은 추가할 수 없음
            if (originalCard.getUser().equals(currentUser)) {
                redirectAttributes.addFlashAttribute("error", "자신의 명함은 추가할 수 없습니다.");
                return "redirect:/c/" + identifier;
            }
            
            // 이미 추가된 명함인지 확인
            boolean alreadyExists = businessCardService.isCardAlreadyExists(
                originalCard.getOwnerUsername(), currentUser);
            
            if (alreadyExists) {
                redirectAttributes.addFlashAttribute("error", "이미 추가된 명함입니다.");
                return "redirect:/c/" + identifier;
            }
            
            // 새 명함 생성 및 저장
            BusinessCard newCard = new BusinessCard();
            newCard.setName(originalCard.getName());
            newCard.setCompany(originalCard.getCompany());
            newCard.setDepartment(originalCard.getDepartment());
            newCard.setPosition(originalCard.getPosition());
            newCard.setPhone(originalCard.getPhone());
            newCard.setEmail(originalCard.getEmail());
            newCard.setDescription(originalCard.getDescription());
            newCard.setUser(currentUser);
            newCard.setOwner(false);
            newCard.setOwnerUsername(originalCard.getOwnerUsername());
            
            businessCardRepository.save(newCard);
            
            redirectAttributes.addFlashAttribute("success", "명함이 추가되었습니다.");
            return "redirect:/c/" + identifier;
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "명함 추가 중 오류가 발생했습니다.");
            return "redirect:/c/" + identifier;
        }
    }

    // 내 QR 코드 보기 (로그인 필요)
    @GetMapping("/manage/qr")
    public String showMyQRCode(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsername(auth.getName());
            
            UserQRCode userQRCode = userQRCodeRepository.findByUser(currentUser)
                .orElse(new UserQRCode());
                
            if (userQRCode.getQrCodeIdentifier() == null) {
                userQRCode.setUser(currentUser);
                userQRCode.setQrCodeIdentifier(UUID.randomUUID().toString());
                userQRCode = userQRCodeRepository.save(userQRCode);
            }
            
            String qrCodeUrl = domain + "/c/" + userQRCode.getQrCodeIdentifier();
            
            // URL 인코딩 처리
            String encodedUrl = URLEncoder.encode(qrCodeUrl, StandardCharsets.UTF_8.toString())
                .replace("+", "%20"); // 공백 문자 처리
            
            model.addAttribute("qrCodeUrl", qrCodeUrl);
            model.addAttribute("encodedUrl", encodedUrl);
            return "qr/my-qr";
            
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
    
    private class RedirectResponse {
        private String redirectUrl;
        
        public RedirectResponse(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }
        
        public String getRedirectUrl() {
            return redirectUrl;
        }
    }
}