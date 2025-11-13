package pricing.policy;

import java.math.BigDecimal;

public class JejuPricePolicy implements PricePolicy {
    @Override public BigDecimal apply(BigDecimal base) {
        return base.multiply(new BigDecimal("1.12")); // 12% 할증, 관광지 할증
    }
    @Override public String name() { return "Jeju Policy (+12% surcharge)"; }
}
