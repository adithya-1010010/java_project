package com.movieticketbooking.model.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountedPricing implements PricingStrategy {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final int minTickets;
    private final BigDecimal discountPercent;

    public DiscountedPricing(int minTickets, BigDecimal discountPercent) {
        if (minTickets <= 0) {
            throw new IllegalArgumentException("minTickets must be positive");
        }
        if (discountPercent == null
                || discountPercent.signum() < 0
                || discountPercent.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalArgumentException("discountPercent must be between 0 and 100");
        }
        this.minTickets = minTickets;
        this.discountPercent = discountPercent;
    }

    @Override
    public BigDecimal price(BigDecimal basePrice, int ticketCount) {
        if (basePrice == null || basePrice.signum() < 0) {
            throw new IllegalArgumentException("basePrice must be a non-negative value");
        }
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("ticketCount must be positive");
        }
        BigDecimal full = basePrice.multiply(BigDecimal.valueOf(ticketCount));
        if (ticketCount < minTickets) {
            return full.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal discountRate = discountPercent.divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.subtract(discountRate);
        return full.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}