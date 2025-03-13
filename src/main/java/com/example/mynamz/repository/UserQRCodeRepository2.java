package com.example.mynamz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mynamz.model.entity.User;
import com.example.mynamz.model.entity.UserQRCode;

public interface UserQRCodeRepository2 extends JpaRepository<UserQRCode, Long> {
    UserQRCode findByUser(User user);
    UserQRCode findByQrCodeIdentifier(String identifier);
}