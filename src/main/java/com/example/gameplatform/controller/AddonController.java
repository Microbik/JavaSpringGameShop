package com.example.gameplatform.controller;

import com.example.gameplatform.model.Addon;
import com.example.gameplatform.service.AddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addons")
@RequiredArgsConstructor
public class AddonController {

    private final AddonService addonService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Addon>> getAllAddons() {
        return ResponseEntity.ok(addonService.getAllAddons());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Addon> getAddonById(@PathVariable Long id) {
        return ResponseEntity.ok(addonService.getAddonById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Addon> createAddon(@RequestBody Addon addon) {
        return ResponseEntity.ok(addonService.createAddon(addon));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Addon> updateAddon(
            @PathVariable Long id,
            @RequestBody Addon addonDetails
    ) {
        return ResponseEntity.ok(addonService.updateAddon(id, addonDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAddon(@PathVariable Long id) {
        addonService.deleteAddon(id);
        return ResponseEntity.noContent().build();
    }
}