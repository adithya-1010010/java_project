package com.movieticketbooking.model.pricing;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal price(BigDecimal basePrice, int ticketCount);
}