package com.example.gameplatform.controller;

import com.example.gameplatform.dto.BalanceTopUpRequest;
import com.example.gameplatform.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.gameplatform.service.SecurityUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping("/top-up")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> topUpBalance(
            @RequestBody BalanceTopUpRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        // Проверка прав: админ или собственный аккаунт
        if (!currentUser.isAdmin() && !currentUser.getId().equals(request.userId())) {
            throw new AccessDeniedException("Вы можете пополнять только свой баланс");
        }

        balanceService.topUpBalance(request.userId(), request.amount());
        return ResponseEntity.ok("Баланс успешно пополнен на " + request.amount());
    }
}