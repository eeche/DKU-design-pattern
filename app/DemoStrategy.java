package app;

import domain.PaymentMethod;
import domain.Tier;
import domain.Region;
import payment.CardPaymentStrategy;
import payment.CashPaymentStrategy;
import payment.TransferPaymentStrategy;
import pricing.booking.Booking;
import pricing.strategy.BasicTierStrategy;
import pricing.strategy.PremiumTierStrategy;
import pricing.strategy.ProTierStrategy;

import java.math.BigDecimal;

public final class DemoStrategy {
    public static void main(String[] args) {
        // Region은 이 데모에선 의미 없으니 SEOUL 고정이라고 가정(Booking 생성자에 따라 수정)
        Booking a1 = new Booking(new BigDecimal("12000"), 2, Tier.BASIC, PaymentMethod.CARD, Region.SEOUL);
        Booking a2 = new Booking(new BigDecimal("12000"), 3, Tier.PRO, PaymentMethod.CASH, Region.SEOUL);
        Booking a3 = new Booking(new BigDecimal("12000"), 5, Tier.PREMIUM, PaymentMethod.TRANSFER, Region.SEOUL);

        a1.setTierStrategy(new BasicTierStrategy());
        a1.setPaymentStrategy(new CardPaymentStrategy());

        a2.setTierStrategy(new ProTierStrategy());
        a2.setPaymentStrategy(new CashPaymentStrategy());

        a3.setTierStrategy(new PremiumTierStrategy());
        a3.setPaymentStrategy(new TransferPaymentStrategy());

        run(a1);
        run(a2);
        run(a3);

        // 런타임 전략 교체
        a1.setTierStrategy(new PremiumTierStrategy());
        a1.setPaymentStrategy(new TransferPaymentStrategy());
        run(a1);
    }

    private static void run(Booking b) {
        BigDecimal total = b.quote();
        System.out.printf("%s/%s (%dh) → %,8.0f원%n",
                b.tier(), b.payment(), b.hours(), total);
        b.pay(total);
        System.out.println();
    }
}
