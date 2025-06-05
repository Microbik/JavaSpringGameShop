package com.example.gameplatform.service;

import com.example.gameplatform.dto.PurchaseResult;
import com.example.gameplatform.model.Game;
import com.example.gameplatform.model.User;
import com.example.gameplatform.model.UserGame;
import com.example.gameplatform.repository.GameRepository;
import com.example.gameplatform.repository.UserGameRepository;
import com.example.gameplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;

    public PurchaseService(UserRepository userRepository,
                           GameRepository gameRepository,
                           UserGameRepository userGameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userGameRepository = userGameRepository;
    }

    @Transactional
    public PurchaseResult purchaseGame(Long userId, Long gameId) {
        // 1. Проверка существования игры у пользователя
        if (userGameRepository.existsByUserIdAndGameId(userId, gameId)) {
            return new PurchaseResult(false, "Игра уже куплена ранее");
        }

        // 2. Получение сущностей (оптимизировано для транзакции)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        // 3. Проверка баланса
        if (user.getBalance().compareTo(game.getPrice()) < 0) {
            return new PurchaseResult(false, "Недостаточно средств");
        }

        // 4. Транзакционные операции
        // Списание средств
        user.setBalance(user.getBalance().subtract(game.getPrice()));
        userRepository.save(user); // Обновляем баланс

        // Создание записи о покупке
        UserGame userGame = new UserGame(user, game);
        userGameRepository.save(userGame);

        return new PurchaseResult(true, "Покупка успешно завершена");
    }
}
