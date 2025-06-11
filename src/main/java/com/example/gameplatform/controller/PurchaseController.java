package com.example.gameplatform.controller;

import com.example.gameplatform.dto.PurchaseResult;
import com.example.gameplatform.service.PurchaseService;
import com.example.gameplatform.service.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<PurchaseResult> purchaseGame(@RequestBody Map<String, Long> payload) {
        Long gameId = payload.get("gameId");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();

        PurchaseResult result = purchaseService.purchaseGame(userId, gameId);

        return result.success()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

}
