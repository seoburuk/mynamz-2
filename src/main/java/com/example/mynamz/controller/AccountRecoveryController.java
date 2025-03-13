package com.example.mynamz.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mynamz.model.dto.FindIdRequest;
import com.example.mynamz.model.dto.FindIdResponse;
import com.example.mynamz.model.dto.FindPasswordRequest;
import com.example.mynamz.model.dto.FindPasswordResponse;
import com.example.mynamz.service.AccountRecoveryService;
import com.example.mynamz.service.ResetPasswordRequest;
import com.example.mynamz.service.ResetPasswordResponse;

// 또는
import jakarta.validation.Valid;  // Java 17 이상의 경우

@RestController
@RequestMapping("/api/account")
public class AccountRecoveryController {

    private final AccountRecoveryService accountRecoveryService;

    public AccountRecoveryController(AccountRecoveryService accountRecoveryService) {
        this.accountRecoveryService = accountRecoveryService;
    }

    @PostMapping("/find-id")
    public ResponseEntity<FindIdResponse> findId(@Valid @RequestBody FindIdRequest request) {
        String maskedId = accountRecoveryService.findIdByEmail(request.getEmail());
        return ResponseEntity.ok(new FindIdResponse(maskedId));
    }

    @PostMapping("/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(@Valid @RequestBody FindPasswordRequest request) {
        boolean sent = accountRecoveryService.sendPasswordResetEmail(request.getUsername(), request.getEmail());
        return ResponseEntity.ok(new FindPasswordResponse(sent));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        boolean reset = accountRecoveryService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new ResetPasswordResponse(reset));
    }
}