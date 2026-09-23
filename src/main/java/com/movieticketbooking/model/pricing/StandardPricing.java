package com.movieticketbooking.model.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardPricing implements PricingStrategy {

    @Override
    public BigDecimal price(BigDecimal basePrice, int ticketCount) {
        if (basePrice == null || basePrice.signum() < 0) {
            throw new IllegalArgumentException("basePrice must be a non-negative value");
        }
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("ticketCount must be positive");
        }
        return basePrice
                .multiply(BigDecimal.valueOf(ticketCount))
                .setScale(2, RoundingMode.HALF_UP);
    }
}