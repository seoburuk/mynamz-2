package com.example.mynamz.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mynamz.model.dto.BusinessCardDTO;
import com.example.mynamz.model.entity.BusinessCard;
import com.example.mynamz.model.entity.NFCBusinessCardShare;
import com.example.mynamz.model.entity.User;
import com.example.mynamz.repository.BusinessCardRepository;
import com.example.mynamz.repository.NFCBusinessCardShareRepository;
import com.example.mynamz.repository.UserRepository;
import com.example.mynamz.model.dto.NFCShareRequest;
import com.example.mynamz.model.dto.NFCShareResponse;

@Service
@Transactional
public class NFCBusinessCardService {
    
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;
    private final NFCBusinessCardShareRepository nfcShareRepository;

    public NFCBusinessCardService(BusinessCardRepository businessCardRepository, 
                                UserRepository userRepository,
                                NFCBusinessCardShareRepository nfcShareRepository) {
        this.businessCardRepository = businessCardRepository;
        this.userRepository = userRepository;
        this.nfcShareRepository = nfcShareRepository;
    }

    public NFCShareResponse shareBusinessCard(Long userId, NFCShareRequest request) {
        // 1. 사용자 검증
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. 명함 정보 검증
        BusinessCard businessCard = businessCardRepository.findById(request.getBusinessCardId())
            .orElseThrow(() -> new IllegalArgumentException("Business card not found"));

        // 명함 소유자 확인
        if (!businessCard.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to share this business card");
        }

        // 3. NFC 데이터 생성
        String nfcId = generateNFCId();

        // 4. 공유 정보 저장
        NFCBusinessCardShare nfcShare = new NFCBusinessCardShare();
        nfcShare.setNfcId(nfcId);
        nfcShare.setBusinessCard(businessCard);
        nfcShare.setShareDateTime(LocalDateTime.now());
        nfcShare.setActive(true);

        nfcShareRepository.save(nfcShare);

        return new NFCShareResponse(nfcId, businessCard.getId(), LocalDateTime.now());
    }

    public BusinessCardDTO receiveBusinessCard(String nfcId, Long receiverId) {
        // 1. NFC ID 검증
        NFCBusinessCardShare nfcShare = nfcShareRepository.findByNfcIdAndIsActiveTrue(nfcId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired NFC share"));

        // 2. 수신자 검증
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // 3. 해당 명함 정보 조회
        BusinessCard businessCard = nfcShare.getBusinessCard();

        // 4. 수신자의 명함첩에 추가 (복제된 명함 생성)
        BusinessCard receivedCard = new BusinessCard();
        receivedCard.setName(businessCard.getName());
        receivedCard.setCompany(businessCard.getCompany());
        receivedCard.setPosition(businessCard.getPosition());
        receivedCard.setPhone(businessCard.getPhone());
        receivedCard.setEmail(businessCard.getEmail());
        receivedCard.setDepartment(businessCard.getDepartment());
        receivedCard.setUser(receiver);

        BusinessCard savedCard = businessCardRepository.save(receivedCard);

        // 5. 공유 상태 업데이트
        nfcShare.setActive(false);
        nfcShareRepository.save(nfcShare);

        // 6. DTO 변환 및 반환
        return BusinessCardDTO.fromEntity(savedCard);
    }

    // NFC ID 생성을 위한 유틸리티 메서드
    private String generateNFCId() {
        return UUID.randomUUID().toString();
    }
}


