package observer;

import options.decorator.Room;

public final class SMSNotifier implements RoomObserver {
    private final String number;

    public SMSNotifier(String number) {
        this.number = number;
    }

    @Override
    public void onEvent(RoomEvent e) {
        Room room = e.room();

        String message = switch (e.status()) {
            case RESERVED ->
                "[SMS: %s] 방 %s 예약됨 (시간당 요금: %.0f원)"
                    .formatted(number, room.getRoomID(), room.getBaseRatePerHour());
            case CANCELED ->
                "[SMS: %s] 방 %s 예약 취소됨 (시간당 요금: %.0f원)"
                    .formatted(number, room.getRoomID(), room.getBaseRatePerHour());
        };

        System.out.println(message);
    }
}
