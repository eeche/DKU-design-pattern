package app;

import observer.EmailNotifier;
import observer.ReservationSystem;
import observer.SMSNotifier;
import options.decorator.Room;

/**
 * Observer 패턴 데모:
 * - 방을 등록하고
 * - 예약/취소 시 Email + SMS로 알림 전송
 */
public final class DemoObserver {
    public static void main(String[] args) {
        ReservationSystem reservation = new ReservationSystem();

        // Observer 등록
        reservation.addObserver(new SMSNotifier("010-1234-5678"));
        reservation.addObserver(new EmailNotifier("admin@room.com"));

        // 방 등록
        Room room1 = new Room("D-102", 3, 30000);
        reservation.addRoom(room1);

        Room room2 = new Room("D-101", 2, 25000);
        reservation.addRoom(room2);

        // 예약/취소 시나리오
        reservation.reserve("D-101");
        reservation.reserve("D-102");
        reservation.cancel("D-101");
        reservation.reserve("D-101");
        reservation.cancel("D-101");
    }
}
