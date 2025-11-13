package options.decorator;

// 구체 데코레이터
public class CaffeAddon extends Addon {
    private static final double CAFE_PASS = 15000.0;

    public CaffeAddon(Bookable bookable) {
        super(bookable);
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + ", 카페 이용권 포함";
    }

    @Override
    public double cost(int slot) {
        return wrapped.cost(slot) + CAFE_PASS;
    }
}
