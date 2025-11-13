package pricing.strategy;

import java.math.BigDecimal;

import pricing.PricingContext;

/**
 * Pro 등급: 5% 할인.
 */
public class ProTierStrategy implements TierPricingStrategy {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.95"); // 5% off

    @Override
    public BigDecimal apply(BigDecimal current, PricingContext ctx) {
        if (current == null) throw new IllegalArgumentException("current price is null");
        return current.multiply(DISCOUNT_RATE);
    }
}