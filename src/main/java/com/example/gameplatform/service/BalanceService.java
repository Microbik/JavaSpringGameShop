package com.example.gameplatform.service;

import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;

    @Transactional
    public BigDecimal topUpBalance(Long userId, BigDecimal amount) {
        // Проверяем сумму на валидность
        validateAmount(amount);

        // Блокируем запись пользователя для обновления
        User user = userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Обновляем баланс с округлением
        BigDecimal newBalance = user.getBalance().add(amount)
                .setScale(2, RoundingMode.HALF_UP);

        // Проверяем максимально допустимый баланс
        validateMaxBalance(newBalance);

        user.setBalance(newBalance);
        userRepository.save(user);

        return newBalance;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Проверяем минимальную сумму пополнения (например, не менее 0.01)
        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            throw new IllegalArgumentException("Minimum top-up amount is 0.01");
        }
    }

    private void validateMaxBalance(BigDecimal balance) {
        // Максимальный баланс (например, 1 миллион)
        BigDecimal maxBalance = new BigDecimal("1000000");
        if (balance.compareTo(maxBalance) > 0) {
            throw new IllegalArgumentException("Maximum balance limit exceeded");
        }
    }

    public BigDecimal getCurrentBalance(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId))
                .getBalance();
    }
}