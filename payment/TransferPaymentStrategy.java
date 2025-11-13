package payment;

import java.math.BigDecimal;

public class TransferPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("[TRANSFER] 계좌이체 안내");
        System.out.println("입금 금액 : ₩" + String.format("%,d", amount.intValue()));
        System.out.println("입금 계좌 : 우리은행 1002-123-456789 (공유오피스)");
        System.out.println("입금 확인 후 예약이 확정됩니다.\n");
    }
}
