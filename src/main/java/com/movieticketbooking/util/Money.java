package com.movieticketbooking.util;

import java.math.BigDecimal;

public final class Money {

    private Money() {
    }

    public static long toCents(BigDecimal amount) {
        return amount.movePointRight(2).longValueExact();
    }

    public static BigDecimal fromCents(long cents) {
        return BigDecimal.valueOf(cents, 2);
    }
}