package pricing.strategy;

import java.math.BigDecimal;
import pricing.PricingContext;
import pricing.policy.PricePolicy;

// PricePolicy(지역정책)를 엔진 파이프라인에서 쓰기 위한 래퍼 전략
public class RegionPolicyStrategy implements PricingStrategy {
    private final PricePolicy policy;

    public RegionPolicyStrategy(PricePolicy policy) {
        this.policy = policy;
    }

    @Override
    public BigDecimal apply(BigDecimal current, PricingContext ctx) {
        return policy.apply(current);
    }

    @Override
    public String toString() { return "RegionPolicy(" + policy.name() + ")"; }
}
