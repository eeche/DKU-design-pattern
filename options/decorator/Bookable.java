package options.decorator;

// 추상 컴포넌트
public interface Bookable {
    String getDescription();
    
    double cost(int slot);
}