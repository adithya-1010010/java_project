package com.movieticketbooking.model.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PricingStrategyTest {

    private final StandardPricing standard = new StandardPricing();
    private final DiscountedPricing discounted = new DiscountedPricing(5, new BigDecimal("10"));

    @Test
    void standardPricingMultipliesPriceAndCount() {
        assertEquals(new BigDecimal("24.00"), standard.price(new BigDecimal("12.00"), 2));
    }

    @Test
    void standardPricingRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> standard.price(new BigDecimal("-1"), 2));
        assertThrows(IllegalArgumentException.class, () -> standard.price(new BigDecimal("12.00"), 0));
    }

    @Test
    void discountedPricingFallsBackBelowThreshold() {
        assertEquals(new BigDecimal("36.00"), discounted.price(new BigDecimal("12.00"), 3));
    }

    @Test
    void discountedPricingAppliesDiscountAtThreshold() {
        assertEquals(new BigDecimal("54.00"), discounted.price(new BigDecimal("12.00"), 5));
    }

    @Test
    void discountedPricingRejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> new DiscountedPricing(0, new BigDecimal("10")));
        assertThrows(IllegalArgumentException.class, () -> new DiscountedPricing(5, null));
        assertThrows(IllegalArgumentException.class, () -> new DiscountedPricing(5, new BigDecimal("101")));
    }
}