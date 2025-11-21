package observer;

import options.decorator.Room;

public final class EmailNotifier implements RoomObserver {
    private final String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void onEvent(RoomEvent e) {
        Room room = e.room();

        String subject = "[공유오피스] 예약 현황 업데이트";
        String body = switch (e.status()) {
            case RESERVED ->
                "방 %s (최대 %d인)이 예약되었습니다."
                    .formatted(room.getRoomID(), room.getCapacity());
            case CANCELED ->
                "방 %s (최대 %d인) 예약이 취소되었습니다."
                    .formatted(room.getRoomID(), room.getCapacity());
        };

        System.out.printf("[Email to %s] Subject: %s%n%s%n",
                emailAddress, subject, body);
    }
}
