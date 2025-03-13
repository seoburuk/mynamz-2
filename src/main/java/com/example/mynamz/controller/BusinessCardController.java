package com.example.mynamz.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mynamz.model.dto.BusinessCardDTO;
import com.example.mynamz.model.entity.BusinessCard;
import com.example.mynamz.model.entity.User;
import com.example.mynamz.repository.BusinessCardRepository;
import com.example.mynamz.repository.UserRepository;
import com.example.mynamz.service.BusinessCardService;

@Controller
@RequestMapping("/manage")
public class BusinessCardController {
    
    @Autowired
    private BusinessCardRepository businessCardRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BusinessCardService businessCardService;
    
    @GetMapping("/cards/share/{id}")
    public String generateQrCode(@PathVariable Long id, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsername(auth.getName());
            
            BusinessCard card = businessCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
                
            if (!card.getUser().equals(currentUser)) {
                return "redirect:/manage/cards";
            }
            
            String qrUrl = "http://localhost:8080/c/" + id;
            model.addAttribute("qrUrl", qrUrl);
            model.addAttribute("card", BusinessCardDTO.fromEntity(card));
            
            return "card/share-qr";
            
        } catch (Exception e) {
            return "redirect:/manage/cards";
        }
    }

 // 명함 수정 폼 표시
    @GetMapping("/cards/my-profile/edit/{id}")
    public String editMyProfileCard(@PathVariable Long id, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsername(auth.getName());
            
            BusinessCard card = businessCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
                
            // 현재 사용자의 명함인지 확인
            if (!card.getUser().equals(currentUser) || !card.isOwner()) {
                return "redirect:/manage/cards/my-profile";
            }
            
            model.addAttribute("card", BusinessCardDTO.fromEntity(card));
            return "card/edit-profile";
            
        } catch (Exception e) {
            return "redirect:/manage/cards/my-profile";
        }
    }

    // 새 명함 추가 폼 표시
    @GetMapping("/cards/my-profile/edit")
    public String newMyProfileCard(Model model) {
        try {
            model.addAttribute("card", new BusinessCardDTO());
            return "card/edit-profile";
        } catch (Exception e) {
            return "redirect:/manage/cards/my-profile";
        }
    }

    // 명함 수정/추가 처리
    @PostMapping("/cards/my-profile/edit")
    public String updateMyProfileCard(@ModelAttribute BusinessCardDTO cardDTO, 
                                    RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsername(auth.getName());
            
            // 새 명함인 경우 개수 체크
            if (cardDTO.getId() == null) {
                int cardCount = businessCardRepository.countMyCards(currentUser);
                if (cardCount >= 4) {
                    redirectAttributes.addFlashAttribute("error", "최대 4개의 명함만 등록할 수 있습니다.");
                    return "redirect:/manage/cards/my-profile";
                }
            }
            
            // 기존 명함인 경우 소유권 확인
            else {
                BusinessCard existingCard = businessCardRepository.findById(cardDTO.getId())
                    .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
                    
                if (!existingCard.getUser().equals(currentUser) || !existingCard.isOwner()) {
                    redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
                    return "redirect:/manage/cards/my-profile";
                }
            }
            
            // 명함 저장
            BusinessCard card = cardDTO.getId() == null ? new BusinessCard() : businessCardRepository.findById(cardDTO.getId()).get();
            card.setName(cardDTO.getName());
            card.setCompany(cardDTO.getCompany());
            card.setDepartment(cardDTO.getDepartment());
            card.setPosition(cardDTO.getPosition());
            card.setPhone(cardDTO.getPhone());
            card.setEmail(cardDTO.getEmail());
            card.setDescription(cardDTO.getDescription());
            card.setUser(currentUser);
            card.setOwner(true);
            card.setOwnerUsername(currentUser.getUsername());
            
            businessCardRepository.save(card);
            
            redirectAttributes.addFlashAttribute("success", 
                cardDTO.getId() == null ? "명함이 추가되었습니다." : "명함이 수정되었습니다.");
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "명함 저장 중 오류가 발생했습니다.");
        }
        
        return "redirect:/manage/cards/my-profile";
    }
    
    // 명함 목록 조회
    @GetMapping("/cards")
    public String listCards(Model model,
                          @RequestParam(required = false) String searchType,
                          @RequestParam(required = false) String keyword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);
        
        List<BusinessCard> cards;
        if (keyword != null && !keyword.trim().isEmpty()) {
            switch(searchType) {
                case "name":
                    cards = businessCardRepository.searchByName(keyword, currentUser.getId());
                    break;
                case "company":
                    cards = businessCardRepository.searchByCompany(keyword, currentUser.getId());
                    break;
                case "department":
                    cards = businessCardRepository.searchByDepartment(keyword, currentUser.getId());
                    break;
                case "description":
                    cards = businessCardRepository.searchByDescription(keyword, currentUser.getId());
                    break;
                default:
                    cards = businessCardRepository.searchByKeyword(keyword, currentUser.getId());
            }
        } else {
            cards = businessCardRepository.findReceivedCards(currentUser.getId());
        }
         // 디버깅을 위한 로그 추가
    System.out.println("=== Cards Debug Log ===");
    cards.forEach(card -> {
        System.out.println(String.format(
            "Card ID: %d, Name: %s, Description: %s",
            card.getId(),
            card.getName(),
            card.getDescription()
        ));
    });

    List<BusinessCardDTO> cardDTOs = cards.stream()
            .map(BusinessCardDTO::fromEntity)
            .collect(Collectors.toList());

    // DTO 변환 후 로그
    System.out.println("=== DTOs Debug Log ===");
    cardDTOs.forEach(dto -> {
        System.out.println(String.format(
            "DTO ID: %d, Name: %s, Description: %s",
            dto.getId(),
            dto.getName(),
            dto.getDescription()
        ));
    });

        model.addAttribute("otherCards", cardDTOs);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        
        return "card/list";
    }

    // 내 프로필 명함 조회
    @GetMapping("/cards/my-profile")
    public String viewMyCards(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(auth.getName());
        
        List<BusinessCard> myCards = businessCardRepository.findMyCards(currentUser);
        List<BusinessCardDTO> cardDTOs = myCards.stream()
            .map(BusinessCardDTO::fromEntity)
            .collect(Collectors.toList());
            
        model.addAttribute("cards", cardDTOs);

        return "card/my-profile";
    }

    // 명함 추가 폼
    @GetMapping("/cards/add")
    public String addCardForm(Model model) {
        model.addAttribute("card", new BusinessCardDTO());
        return "card/add";
    }

    // 명함 추가 처리
    @PostMapping("/cards/add")
    public String addCard(@ModelAttribute BusinessCardDTO cardDTO, RedirectAttributes redirectAttributes) {
        try {
            businessCardService.saveMyCard(cardDTO);
            redirectAttributes.addFlashAttribute("success", "명함이 성공적으로 추가되었습니다.");
            return "redirect:/manage/cards";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "명함 추가 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/manage/cards/add";
        }
    }

    // 내 프로필에 명함 추가
    @PostMapping("/cards/my-profile/add")
    public String addMyCard(@ModelAttribute BusinessCardDTO cardDTO, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username);
            
            int cardCount = businessCardRepository.countMyCards(currentUser);
            if (cardCount >= 4) {
                redirectAttributes.addFlashAttribute("error", "최대 4개의 명함만 등록할 수 있습니다.");
                return "redirect:/manage/cards/my-profile";
            }
            
            BusinessCard newCard = cardDTO.toEntity();
            newCard.setOwner(true);
            newCard.setOwnerUsername(username);
            newCard.setUser(currentUser);
            newCard.setDescription(cardDTO.getDescription());
            
            businessCardRepository.save(newCard);
            redirectAttributes.addFlashAttribute("success", "새 명함이 추가되었습니다.");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "명함 추가 중 오류가 발생했습니다.");
        }
        
        return "redirect:/manage/cards/my-profile";
    }

    // 명함 수정 폼
    @GetMapping("/cards/edit/{id}")
    public String editCardForm(@PathVariable Long id, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsername(auth.getName());
            
            BusinessCard card = businessCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
            
            if (!card.getUser().equals(currentUser)) {
                return "redirect:/manage/cards";
            }
            
            model.addAttribute("card", BusinessCardDTO.fromEntity(card));
            return "card/edit";
            
        } catch (Exception e) {
            return "redirect:/manage/cards";
        }
    }

    // 명함 수정 처리
    @PostMapping("/cards/edit/{id}")
public String updateCard(@PathVariable Long id, 
                        @ModelAttribute BusinessCardDTO cardDTO,
                        RedirectAttributes redirectAttributes) {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(auth.getName());
        
        // 디버깅을 위한 로그
        System.out.println("Received DTO description: " + cardDTO.getDescription());
        
        BusinessCard card = businessCardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
        
        if (!card.getUser().equals(currentUser)) {
            redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/manage/cards";
        }
        
        // 기존 description 값 확인
        System.out.println("Original card description: " + card.getDescription());
        
        card.setName(cardDTO.getName());
        card.setCompany(cardDTO.getCompany());
        card.setPosition(cardDTO.getPosition());
        card.setPhone(cardDTO.getPhone());
        card.setEmail(cardDTO.getEmail());
        card.setDepartment(cardDTO.getDepartment());
        card.setDescription(cardDTO.getDescription());
        
        BusinessCard savedCard = businessCardRepository.save(card);
        
        // 저장된 description 값 확인
        System.out.println("Saved card description: " + savedCard.getDescription());
        
        redirectAttributes.addFlashAttribute("success", "명함이 성공적으로 수정되었습니다.");
        return "redirect:/manage/cards";
        
    } catch (Exception e) {
        e.printStackTrace();  // 상세 에러 로그
        redirectAttributes.addFlashAttribute("error", "명함 수정 중 오류가 발생했습니다: " + e.getMessage());
        return "redirect:/manage/cards/edit/" + id;
    }
}

    // 명함 삭제
    @GetMapping("/cards/delete/{id}")  
    public String deleteCard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            businessCardService.deleteCard(id);
            redirectAttributes.addFlashAttribute("success", "명함이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "명함 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/manage/cards";  
    }

    // 내 프로필 명함 삭제
    @PostMapping("/cards/my-profile/delete/{id}")
    public ResponseEntity<?> deleteMyCard(@PathVariable Long id) {
        try {
            BusinessCard card = businessCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
                
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userRepository.findByUsername(auth.getName());
            
            if (!card.getUser().equals(currentUser) || !card.isOwner()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            businessCardRepository.delete(card);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("명함 삭제 중 오류가 발생했습니다.");
        }
    }

    // 명함 저장
    @PostMapping("/cards/save")
    public String saveCard(@RequestParam Long cardId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "명함이 저장되었습니다.");
        return "redirect:/cards/";  
    }
}