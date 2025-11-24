package domain;

import options.decorator.Room;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private final String id;
    private final String userId;
    private final Room room;
    private final LocalDateTime checkIn;  // 시작 시간
    private final LocalDateTime checkOut; // 종료 시간
    private RoomStatus status;

    public Reservation(String userId, Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.userId = userId;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = RoomStatus.RESERVED;
    }

    // [핵심 로직] 이 예약과 다른 기간이 겹치는지 확인
    public boolean isOverlapping(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return this.checkIn.isBefore(otherEnd) && otherStart.isBefore(this.checkOut);
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public Room getRoom() { return room; }
    public RoomStatus getStatus() { return status; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("[예약#%s] %s | 기간: %s ~ %s | 상태: %s",
                id, room.getRoomID(), checkIn, checkOut, status);
    }
}