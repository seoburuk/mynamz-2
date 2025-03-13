package com.example.mynamz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mynamz.model.entity.User;
import com.example.mynamz.model.entity.UserQRCode;

public interface UserQRCodeRepository extends JpaRepository<UserQRCode, Long> {
	Optional<UserQRCode> findByUser(User user);
    Optional<UserQRCode> findByQrCodeIdentifier(String identifier);
    
}