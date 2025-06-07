package com.example.gameplatform.controller;

import com.example.gameplatform.model.Game;
import com.example.gameplatform.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Game> getAll() {
        return gameService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Game> getById(@PathVariable Long id) {
        return ResponseEntity.of(gameService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GameManager', 'ADMIN')")
    public ResponseEntity<Game> create(@RequestBody Game game) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.create(game));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GameManager', 'ADMIN')")
    public ResponseEntity<Game> update(@PathVariable Long id, @RequestBody Game game) {
        return ResponseEntity.of(gameService.update(id, game));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
