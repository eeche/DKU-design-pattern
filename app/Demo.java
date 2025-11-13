package app;

import domain.Region;
import domain.Tier;
import options.decorator.Bookable;
import options.decorator.CaffeAddon;
import options.decorator.LockerAddon;
import options.decorator.Room;
import domain.PaymentMethod;
import payment.CardPaymentStrategy;
import pricing.PricingContext;
import pricing.PricingEngine;
import pricing.factory.RegionPricePolicyFactory;
import pricing.policy.PricePolicy;
import pricing.strategy.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        // 1) 기본 Booking 정보
        BigDecimal hourly = new BigDecimal("20000");
        int hours = 5;
        Tier tier = Tier.PREMIUM;
        PaymentMethod method = PaymentMethod.CARD;
        Region region = Region.SEOUL;

        BigDecimal baseAmount = hourly.multiply(BigDecimal.valueOf(hours));
        PricingContext ctx = new PricingContext(baseAmount, tier, method);

        // 2) Region 정책 → Strategy로 감싸기
        PricePolicy regionPolicy = new RegionPricePolicyFactory().create(region);
        PricingStrategy regionStrategy = new RegionPolicyStrategy(regionPolicy);

        // 3) Tier 전략
        PricingStrategy tierStrategy = new PremiumTierStrategy();

        // 4) Decorator로 옵션 조합 → OptionsStrategy로 감싸기
        Bookable optionChain = new Room("305", 6, hourly.doubleValue());
        optionChain = new CaffeAddon(optionChain);
        optionChain = new LockerAddon(optionChain, "LARGE", 1);
        PricingStrategy optionsStrategy = new OptionsStrategy(optionChain, hours);

        // 5) 파이프라인 구성 (순서 중요)
        List<PricingStrategy> pipeline = new ArrayList<>();
        pipeline.add(regionStrategy);
        pipeline.add(tierStrategy);
        pipeline.add(optionsStrategy);

        PricingEngine engine = new PricingEngine(pipeline);
        BigDecimal total = engine.quote(ctx);

        System.out.printf("Region=%s, Tier=%s, Options=%s%n",
                region, tier, optionChain.getDescription());
        System.out.printf("최종 요금: %, .0f원%n", total.doubleValue());

        // 6) 결제 전략 적용 예시
        var payment = new CardPaymentStrategy();
        payment.pay(total);
    }
}
