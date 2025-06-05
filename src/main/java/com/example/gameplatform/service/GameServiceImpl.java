package com.example.gameplatform.service;

import com.example.gameplatform.model.Game;
import com.example.gameplatform.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    @Override
    public Optional<Game> getById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game create(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> update(Long id, Game game) {
        return gameRepository.findById(id).map(existing -> {
            existing.setName(game.getName());
            existing.setGenre(game.getGenre());
            existing.setPrice(game.getPrice());
            existing.setPublisher(game.getPublisher());
            existing.setRating(game.getRating());
            existing.setReleaseDate(game.getReleaseDate());
            return gameRepository.save(existing);
        });
    }

    @Override
    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}
