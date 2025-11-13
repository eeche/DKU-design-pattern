package pricing.strategy;

import java.math.BigDecimal;

import pricing.PricingContext;

public interface TierPricingStrategy extends PricingStrategy {
    @Override
    BigDecimal apply(BigDecimal current, PricingContext ctx);
}
