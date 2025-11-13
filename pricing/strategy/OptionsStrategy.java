package pricing.strategy;

import java.math.BigDecimal;

import options.decorator.Bookable;
import pricing.PricingContext;

// Decorator(Bookable)로 계산한 옵션 비용을 PricingEngine 파이프라인에 녹여 넣는 Strategy.
public class OptionsStrategy implements PricingStrategy {
    private final Bookable chain;
    private final int slotHours;

    public OptionsStrategy(Bookable chain, int slotHours) {
        this.chain = chain;
        this.slotHours = slotHours;
    }

    @Override
    public BigDecimal apply(BigDecimal current, PricingContext ctx) {
        // Decorator 체인으로 옵션 비용 계산 (double) → BigDecimal로 변환 후 합산
        double add = chain.cost(slotHours);
        return current.add(BigDecimal.valueOf(add));
    }

    @Override
    public String toString() {
        return "OptionStrategy(" + chain.getDescription() + ")";
    }
}
