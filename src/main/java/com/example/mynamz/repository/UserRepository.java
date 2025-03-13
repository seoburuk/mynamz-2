package com.example.mynamz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mynamz.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    User findByQrCode(String qrCode);
    Boolean existsByEmail(String email);
    Optional<User> findByUsernameAndEmail(String username, String email);
    
}