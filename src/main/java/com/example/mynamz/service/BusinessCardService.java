package com.example.mynamz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mynamz.model.dto.BusinessCardDTO;
import com.example.mynamz.model.entity.BusinessCard;
import com.example.mynamz.model.entity.User;
import com.example.mynamz.repository.BusinessCardRepository;
import com.example.mynamz.repository.UserRepository;


@Service
@Transactional
public class BusinessCardService {
    @Autowired
    private BusinessCardRepository cardRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Optional<BusinessCard> getCardByUsername(String username) {
        try {
            return cardRepository.findByOwnerUsername(username);
        } catch (Exception e) {
            // 로깅 추가
            return null;
        }
    }
    
    public void saveCardAfterLogin(Long cardId, String username) {
        User currentUser = userRepository.findByUsername(username);
        Optional<BusinessCard> originalCardOpt = cardRepository.findById(cardId);
        
        if (!originalCardOpt.isPresent()) {
            throw new RuntimeException("명함을 찾을 수 없습니다.");
        }
        
        BusinessCard originalCard = originalCardOpt.get();
            
        // 자신의 명함이거나 이미 추가된 명함인지 확인
        if (originalCard.getUser().equals(currentUser) || 
            cardRepository.existsByOwnerUsernameAndUser(originalCard.getOwnerUsername(), currentUser)) {
            throw new RuntimeException("추가할 수 없는 명함입니다.");
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
        
        cardRepository.save(newCard);
    }

    public boolean isCardAlreadyExists(String ownerUsername, User user) {
        return cardRepository.existsByOwnerUsernameAndUser(ownerUsername, user);
    }

    
    // QR 코드로 직접 명함 조회
    public Optional<BusinessCard> getCardByQrCode(String qrCode) {
        User user = userRepository.findByQrCode(qrCode);
        if (user != null) {
            return getCardByUsername(user.getUsername());
        }
        return null;
    }
    public BusinessCard saveMyCard(BusinessCardDTO cardDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(auth.getName());

        BusinessCard card = cardDTO.toEntity();
        card.setUser(currentUser);
        return cardRepository.save(card);  // BusinessCardRepository -> cardRepository
    }

    public BusinessCard getCard(Long id) {
        return cardRepository.findById(id)  // BusinessCardRepository -> cardRepository
            .orElseThrow(() -> new RuntimeException("명함을 찾을 수 없습니다."));
    }

    public BusinessCard getMyCard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(auth.getName());
        return cardRepository.findByUserAndIsOwnerTrue(currentUser);  // BusinessCardRepository -> cardRepository
    }

    public void updateCard(BusinessCard card) {
        cardRepository.save(card);  // BusinessCardRepository -> cardRepository
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);  // BusinessCardRepository -> cardRepository
    }


   

   
}