package options.decorator;

// 구체 데코레이터
public class LockerAddon extends Addon {
    private enum LockerSize {
        SMALL(1500.0),
        MEDIUM(2500.0),
        LARGE(3500.0);

        private final double ratePerSlot;

        LockerSize(double ratePerSlot) {
            this.ratePerSlot = ratePerSlot;
        }

        public double getRatePerSlot() {
            return ratePerSlot;
        }

        public String getName() {
            return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
        }
    }

    private final LockerSize size;
    private final int count;

    public LockerAddon(Bookable bookable, String size, int count) {
        super(bookable);
        
        LockerSize tempSize;

        try {
            tempSize = LockerSize.valueOf(size.toUpperCase());
        } catch (IllegalArgumentException e) {
            tempSize = LockerSize.MEDIUM;
            System.err.println("[ADMIN LOG] WARNING: Invalid Locker size '" + size + "' provided. Defaulthing to MEDIUM.");
        }

        this.size = tempSize;
        this.count = count;
    }
    

    @Override
    public String getDescription() {
        return wrapped.getDescription() + ", 사물함 사용 (" + size.getName() + " x" + count + "개)";
    }

    @Override
    public double cost(int slot) {
        double lockerCost = size.getRatePerSlot() * slot * count;
        return wrapped.cost(slot) + lockerCost;
    }
}
