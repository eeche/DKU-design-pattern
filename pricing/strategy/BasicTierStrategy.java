package pricing.strategy;

import java.math.BigDecimal;

import pricing.PricingContext;

/**
 * Basic 등급: 할인 없음.
 */
public class BasicTierStrategy implements TierPricingStrategy {

    @Override
    public BigDecimal apply(BigDecimal current, PricingContext ctx) {
        if (current == null) throw new IllegalArgumentException("current price is null");
        return current;
    }
}