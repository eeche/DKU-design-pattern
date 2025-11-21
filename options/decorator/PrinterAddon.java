package options.decorator;

// 구체 데코레이터
public class PrinterAddon extends Addon {
    private enum PrintType {
        MONOCHROME("흑백", 50.0),
        COLOR("컬러", 300.0);

        private final String name;
        private final double pricePerPage;

        PrintType(String name, double pricePerPage) {
            this.name = name;
            this.pricePerPage = pricePerPage;
        }

        public double getPricePerPage() {
            return pricePerPage;
        }

        public String getName() {
            return name;
        }
    }

    private final PrintType type;
    private final int pageCount;

    public PrinterAddon(Bookable bookable, String type, int pageCount) {
        super(bookable);

        PrintType tempType;
        try {
            tempType = PrintType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            tempType = PrintType.MONOCHROME;
            System.err.println("[ADMIN LOG] WARNING: Invalid Print type");
        }

        this.type = tempType;
        this.pageCount = pageCount;
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + 
            ", 프린트기 사용 가능 (" + type.getName() + " x" + pageCount + "장)";
    }

    @Override
    public double cost(int slot) {
        double printerCost = type.getPricePerPage() * pageCount;
        return wrapped.cost(slot) + printerCost;
    }
}
