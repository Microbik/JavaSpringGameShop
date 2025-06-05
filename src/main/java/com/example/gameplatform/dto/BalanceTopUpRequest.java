package com.example.gameplatform.dto;

import java.math.BigDecimal;

public record BalanceTopUpRequest(Long userId, BigDecimal amount) {}
