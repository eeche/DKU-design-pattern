package options.decorator;

// 구체 컴포넌트
public class Room implements Bookable {
    private String roomID;
    private int capacity;
    private double baseRatePerHour;
    
    public Room(String roomID, int capacity, double baseRatePerHour) {
        this.roomID = roomID;
        this.capacity = capacity;
        this.baseRatePerHour = baseRatePerHour;
    }

    @Override
    public String getDescription() {
        return "회의실 No." + roomID + "(최대 " + capacity + "인)";
    }

    @Override
    public double cost(int slot) {
        return baseRatePerHour * slot;
    }
}
