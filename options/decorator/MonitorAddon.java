package options.decorator;

// 구체 데코레이터
public class MonitorAddon extends Addon {
    private static final double PRICE_PER_USE = 5000.0;
    
    public MonitorAddon(Bookable bookable) {
        super(bookable);
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + ", 모니터 사용 포함";
    }

    @Override
    public double cost(int slot) {
        return wrapped.cost(slot) + PRICE_PER_USE;
    }
}
