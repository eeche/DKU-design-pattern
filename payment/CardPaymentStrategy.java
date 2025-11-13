package payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("=================================");
        System.out.println("         [카드 결제 영수증]");
        System.out.println("=================================");
        System.out.println("결제 일시 : " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("결제 수단 : 신용/체크카드");
        System.out.println("결제 금액 : ₩" + String.format("%,d", amount.intValue()));
        System.out.println("승인 번호 : " + (int)(Math.random() * 900000 + 100000));
        System.out.println("=================================\n");
    }
}
