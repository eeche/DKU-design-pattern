package options.decorator;

// 추상 데코레이터
public abstract class Addon implements Bookable {
    protected Bookable wrapped;
    
    public Addon(Bookable bookable) {
        this.wrapped = bookable;
    }

    @Override
    public abstract String getDescription();

    @Override
    public abstract double cost(int slot);
}
