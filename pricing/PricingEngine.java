package pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import pricing.strategy.PricingStrategy;

public final class PricingEngine {
    private final List<PricingStrategy> pipeline;

    public PricingEngine(List<PricingStrategy> pipeline) {
        if (pipeline == null || pipeline.isEmpty()) {
            throw new IllegalArgumentException("pipeline must not be empty");
        }
        this.pipeline = List.copyOf(pipeline);
    }

    /** baseAmount를 시작값으로 받아 전략을 순서대로 적용하고, 최종 금액은 원단위 반올림. */
    public BigDecimal quote(PricingContext ctx) {
        BigDecimal total = ctx.baseAmount;
        for (PricingStrategy s : pipeline) {
            total = s.apply(total, ctx);
        }
        return total.setScale(0, RoundingMode.HALF_UP);
    }
}