package pricing.strategy;

import java.math.BigDecimal;

import pricing.PricingContext;

public interface PricingStrategy {
    BigDecimal apply(BigDecimal current, PricingContext ctx);
}
