package pricing.booking;

import domain.PaymentMethod;
import domain.Tier;
import domain.Region;   
import payment.PaymentStrategy;
import pricing.PricingContext;
import pricing.PricingEngine;
import pricing.strategy.TierPricingStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class Booking {
    private final BigDecimal hourlyRate;   // 시간당 요금
    private final int hours;               // 이용 시간
    private final Tier tier;               // 회원 등급
    private final PaymentMethod payment;   // 결제 수단
    private final Region region;           // 지역

    // ⬇️ 객체가 직접 보유하는 전략
    private TierPricingStrategy tierStrategy;
    private PaymentStrategy paymentStrategy;

    public Booking(BigDecimal hourlyRate, int hours, Tier tier, PaymentMethod payment, Region region) {
        if (hourlyRate == null || hourlyRate.signum() < 0)
            throw new IllegalArgumentException("hourlyRate must be >= 0");
        if (hours <= 0) throw new IllegalArgumentException("hours must be > 0");
        this.hourlyRate = hourlyRate;
        this.hours = hours;
        this.tier = tier;
        this.payment = payment;
        this.region = Objects.requireNonNull(region, "region");
    }

    // --- 전략 주입(setBehavior 스타일) ---
    public void setTierStrategy(TierPricingStrategy tierStrategy) {
        this.tierStrategy = Objects.requireNonNull(tierStrategy);
    }
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = Objects.requireNonNull(paymentStrategy);
    }

    // --- 도메인 정보 ---
    public BigDecimal hourlyRate() { return hourlyRate; }
    public int hours() { return hours; }
    public Tier tier() { return tier; }
    public PaymentMethod payment() { return payment; }
    public Region region() { return region; }

    // 기준 금액 = 시간당 요금 × 시간
    public BigDecimal baseAmount() {
        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }

    // --- 전략 메서드(객체 자신이 호출) ---
    /** 등급 전략을 적용해 최종 금액을 계산 */
    public BigDecimal quote() {
        if (tierStrategy == null) throw new IllegalStateException("Tier strategy not set");
        PricingEngine engine = new PricingEngine(List.of(tierStrategy));
        PricingContext ctx = new PricingContext(baseAmount(), tier, payment);
        return engine.quote(ctx);
    }

    /** 결제 전략으로 출력/처리 */
    public void pay(BigDecimal amount) {
        if (paymentStrategy == null) throw new IllegalStateException("Payment strategy not set");
        paymentStrategy.pay(amount);
    }

    @Override
    public String toString() {
        return String.format("%d시간 × %,d원/시간 (등급:%s, 결제:%s)",
                hours, hourlyRate.intValue(), tier, payment);
    }
}
