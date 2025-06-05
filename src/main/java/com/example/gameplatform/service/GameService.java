package com.example.gameplatform.service;

import com.example.gameplatform.model.Game;
import java.util.*;

public interface GameService {
    List<Game> getAll();
    Optional<Game> getById(Long id);
    Game create(Game game);
    Optional<Game> update(Long id, Game game);
    void delete(Long id);
}
