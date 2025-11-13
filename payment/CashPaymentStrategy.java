package payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("=================================");
        System.out.println("          [현금 영수증]");
        System.out.println("=================================");
        System.out.println("결제 일시 : " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("결제 수단 : 현금 결제");
        System.out.println("결제 금액 : ₩" + String.format("%,d", amount.intValue()));
        System.out.println("발행자 : 공유오피스 매장");
        System.out.println("=================================\n");
    }
}
