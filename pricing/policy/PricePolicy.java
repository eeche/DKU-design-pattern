package pricing.policy;

import java.math.BigDecimal;

public interface PricePolicy {
    /** 지역 정책 적용(옵션/등급 이전 단계) */
    BigDecimal apply(BigDecimal base);
    String name();
}
