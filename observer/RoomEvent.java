package observer;

import domain.RoomStatus;
import options.decorator.Room;

import java.time.Instant;
import java.util.Objects;

public final class RoomEvent {
    private final RoomStatus status;
    private final Room room;
    private final Instant timestamp;

    private RoomEvent(RoomStatus status, Room room) {
        this.status = Objects.requireNonNull(status, "status");
        this.room = Objects.requireNonNull(room, "room");
        this.timestamp = Instant.now();
    }

    public static RoomEvent reserved(Room room) {
        return new RoomEvent(RoomStatus.RESERVED, room);
    }

    public static RoomEvent canceled(Room room) {
        return new RoomEvent(RoomStatus.CANCELED, room);
    }

    public RoomStatus status() { return status; }
    public Room room() { return room; }
    public Instant timestamp() { return timestamp; }
}
