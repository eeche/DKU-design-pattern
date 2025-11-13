package options.bridge;

import java.math.BigDecimal;

import options.decorator.Bookable;

// Decorator(Bookable)의 double 비용을 BigDecimal로 연결해주는 브리지
public final class OptionsCostAdapter {
    // slot(시간) 기준으로 옵션 총액을 BigDecimal로 반환
    public static BigDecimal optionsTotal(Bookable bookable, int slotHours) {
        double cost = bookable.cost(slotHours); // Decorator 체인 최종 금액
        return BigDecimal.valueOf(cost);
    }
}