package com.example.mynamz.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mynamz.model.entity.NFCBusinessCardShare;

@Repository
public interface NFCBusinessCardShareRepository extends JpaRepository<NFCBusinessCardShare, Long> {
    Optional<NFCBusinessCardShare> findByNfcIdAndIsActiveTrue(String nfcId);
}