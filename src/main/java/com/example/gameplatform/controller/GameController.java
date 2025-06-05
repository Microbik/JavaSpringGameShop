package com.example.gameplatform.controller;

import com.example.gameplatform.model.Game;
import com.example.gameplatform.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping
    public List<Game> getAll() {
        return gameService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getById(@PathVariable Long id) {
        return ResponseEntity.of(gameService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Game> create(@RequestBody Game game) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.create(game));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> update(@PathVariable Long id, @RequestBody Game game) {
        return ResponseEntity.of(gameService.update(id, game));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
