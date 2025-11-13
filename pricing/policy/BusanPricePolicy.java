package pricing.policy;

import java.math.BigDecimal;

public class BusanPricePolicy implements PricePolicy {
    @Override public BigDecimal apply(BigDecimal base) {
        return base.multiply(new BigDecimal("0.87")); // 5% + 8% = 13% 할인, 기본 할인 + 프로모션
    }
    @Override public String name() { return "Busan Policy (5% + 8% promotion)"; }
}
