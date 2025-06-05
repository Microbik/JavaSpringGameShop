package com.example.gameplatform.dto;

public record PurchaseResult(boolean success, String message) {
    // Автоматически создаются:
    // - Поля final boolean success и final String message
    // - Конструктор
    // - Геттеры
    // - Методы equals(), hashCode() и toString()
}
