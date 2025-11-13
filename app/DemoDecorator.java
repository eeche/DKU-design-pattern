package app;

import options.decorator.Bookable;
import options.decorator.CaffeAddon;
import options.decorator.LockerAddon;
import options.decorator.MonitorAddon;
import options.decorator.PrinterAddon;
import options.decorator.Room;

public class DemoDecorator {
    public static void main(String[] args) {
        int slotDuration = 5;
        double baseRate = 20000.0;

        System.out.println("==================================================");
        System.out.println("기본 공간: 회의실 No.305 (5시간, " + (baseRate * slotDuration) + ")");
        System.out.println("==================================================");

        // 1. 모니터 + 사물함 조합
        Bookable combo1 = new Room("305", 6, baseRate);
        combo1 = new MonitorAddon(combo1);
        combo1 = new LockerAddon(combo1, "LARGE", 1);

        System.out.println("조합 1: 모니터 사용 + 사물함 사용");
        System.out.println("서비스 구성: " + combo1.getDescription());
        System.out.println("최종 요금: " + combo1.cost(slotDuration));
        System.out.println("--------------------------------------------------");

        // 2. 카페 + 프린트기 조합
        Bookable combo2 = new Room("305", 6, baseRate);
        combo2 = new CaffeAddon(combo2);
        combo2 = new PrinterAddon(combo2, "MONOCHROME", 50);

        System.out.println("조합 2: 카페 이용권 + 프린트기 사용");
        System.out.println("서비스 구성: " + combo2.getDescription());
        System.out.println("최종 요금: " + combo2.cost(slotDuration));
    }
}
