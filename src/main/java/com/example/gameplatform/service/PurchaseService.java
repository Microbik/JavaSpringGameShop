package com.example.gameplatform.service;

import com.example.gameplatform.dto.PurchaseResult;
import com.example.gameplatform.model.*;
import com.example.gameplatform.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;
    private final AddonRepository addonRepository;
    private final UserAddonRepository userAddonRepository;

    @Transactional
    public PurchaseResult purchaseAddon(Long userId, Long addonId) {
        Optional<Addon> addonOpt = addonRepository.findById(addonId);
        if (addonOpt.isEmpty()) {
            return new PurchaseResult(false, "Addon not found");
        }

        Addon addon = addonOpt.get();
        Long gameId = addon.getGame().getId();

        if (!userGameRepository.existsByUserIdAndGameId(userId, gameId)) {
            return new PurchaseResult(false, "User does not own the base game required for this addon");
        }

        if (userAddonRepository.existsByUserIdAndAddonId(userId, addonId)) {
            return new PurchaseResult(false, "Addon already purchased");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getBalance().compareTo(addon.getPrice()) < 0) {
            return new PurchaseResult(false, "Insufficient funds to buy the addon");
        }

        // Списание средств
        user.setBalance(user.getBalance().subtract(addon.getPrice()));
        userRepository.save(user);

        UserAddon userAddon = new UserAddon(user, addon);
        userAddonRepository.save(userAddon);

        return new PurchaseResult(true, "Addon purchased successfully");
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

