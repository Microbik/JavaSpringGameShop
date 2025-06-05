package com.example.gameplatform.service;

import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;

    @Transactional
    public void topUpBalance(Long userId, BigDecimal amount) {
        // Блокируем запись пользователя для обновления
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Проверяем сумму на валидность
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Обновляем баланс
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
}
