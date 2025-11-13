package pricing.strategy;

import java.math.BigDecimal;

import pricing.PricingContext;

/**
 * Premium 등급: 10% 할인.
 */
public class PremiumTierStrategy implements TierPricingStrategy {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.90"); // 10% off

    @Override
    public BigDecimal apply(BigDecimal current, PricingContext ctx) {
        if (current == null) throw new IllegalArgumentException("current price is null");
        return current.multiply(DISCOUNT_RATE);
    }
}