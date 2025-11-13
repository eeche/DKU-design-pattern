package pricing.policy;

import java.math.BigDecimal;

public class SeoulPricePolicy implements PricePolicy {
    @Override public BigDecimal apply(BigDecimal base) {
        return base.multiply(new BigDecimal("0.90")); // 10% 프로모션
    }
    @Override public String name() { return "Seoul Policy (10% promotion)"; }
}
