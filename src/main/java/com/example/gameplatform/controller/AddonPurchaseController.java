package com.example.gameplatform.controller;

import com.example.gameplatform.dto.PurchaseResult;
import com.example.gameplatform.service.PurchaseService;
import com.example.gameplatform.service.SecurityUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/addon-purchases")
public class AddonPurchaseController {

    private final PurchaseService purchaseService;

    public AddonPurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<PurchaseResult> purchaseAddon(@RequestBody Map<String, Long> payload) {
        Long addonId = payload.get("addonId");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();

        PurchaseResult result = purchaseService.purchaseAddon(userId, addonId);

        return result.success()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }
}
