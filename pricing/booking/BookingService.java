package pricing.booking;

import domain.PaymentMethod;
import domain.Tier;
import domain.Region;
import pricing.PricingContext;
import pricing.PricingEngine;
import pricing.factory.PricePolicyFactory;
import pricing.policy.PricePolicy;
import pricing.strategy.PricingStrategy;
import pricing.strategy.RegionPolicyStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BookingService {
    private List<PricingStrategy> pipeline;
    private final PricePolicyFactory regionFactory;

    public BookingService(List<PricingStrategy> pipeline, PricePolicyFactory factory) {
        setPipeline(pipeline);
        this.regionFactory = Objects.requireNonNull(factory, "factory");
    }

    public void setPipeline(List<PricingStrategy> pipeline) {
        if (pipeline == null || pipeline.isEmpty())
            throw new IllegalArgumentException("pipeline must not be empty");
        this.pipeline = List.copyOf(pipeline);
    }

    // 현재 전략 조합 이름(간단 표기)
    public String currentPlanName() {
        return pipeline.stream()
                .map(s -> s.getClass().getSimpleName())
                .reduce((a, b) -> a + " + " + b)
                .orElse("NoStrategy");
    }

    // Booking 정보를 기반으로 최종 견적 계산
    public BigDecimal quote(Booking b) {
        Objects.requireNonNull(b, "booking");
        BigDecimal base = b.baseAmount();           // 시간당요금 × 시간
        Tier tier = b.tier();
        PaymentMethod method = b.payment();
        Region region = b.region();                 // 지역 정보

       // 지역 정책 → RegionPolicyStrategy 로 감싸 파이프라인 맨 앞에 삽입
        PricePolicy policy = regionFactory.create(region);
        List<PricingStrategy> effective = new ArrayList<>();
        effective.add(new RegionPolicyStrategy(policy));
        effective.addAll(pipeline);

        PricingEngine engine = new PricingEngine(effective);
        PricingContext ctx = new PricingContext(base, tier, method);
        return engine.quote(ctx);
    }
}
