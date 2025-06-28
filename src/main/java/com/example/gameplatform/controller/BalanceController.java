package com.example.gameplatform.controller;

import com.example.gameplatform.dto.BalanceTopUpRequest;
import com.example.gameplatform.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.gameplatform.service.SecurityUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping("/top-up")
    @PreAuthorize("hasRole('ADMIN') or #request.userId == principal.id")
    public ResponseEntity<Map<String, Object>> topUpBalance(
            @RequestBody BalanceTopUpRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        try {
            BigDecimal amount = request.amount().setScale(2, RoundingMode.HALF_UP);
            BigDecimal newBalance = balanceService.topUpBalance(request.userId(), amount);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "Balance successfully topped up by " + amount);
            response.put("newBalance", newBalance);
            response.put("currency", "USD");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BigDecimal> getCurrentBalance(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {
        BigDecimal balance = balanceService.getCurrentBalance(currentUser.getId());
        return ResponseEntity.ok(balance);
    }
}