package com.example.gameplatform.controller;

import com.example.gameplatform.dto.BalanceTopUpRequest;
import com.example.gameplatform.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

    @PostMapping("/top-up")
    @PreAuthorize("hasRole('ADMIN') or #request.userId == principal.id")
    public ResponseEntity<String> topUpBalance(@RequestBody BalanceTopUpRequest request) {
        balanceService.topUpBalance(request.userId(), request.amount());
        return ResponseEntity.ok("Баланс успешно пополнен на " + request.amount());
    }
}
