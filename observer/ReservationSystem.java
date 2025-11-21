package observer;

import options.decorator.Room;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ReservationSystem {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final List<RoomObserver> observers = new ArrayList<>();

    public void addObserver(RoomObserver o) {
        observers.add(Objects.requireNonNull(o));
    }

    public void removeObserver(RoomObserver o) {
        observers.remove(o);
    }

    private void notifyAllObservers(RoomEvent e) {
        for (RoomObserver o : List.copyOf(observers)) {
            o.onEvent(e);
        }
    }

    public void addRoom(Room room) {
        rooms.put(room.getRoomID(), Objects.requireNonNull(room));
    }

    public boolean reserve(String roomId) {
        Room r = rooms.get(roomId);
        if (r == null) return false;

        notifyAllObservers(RoomEvent.reserved(r));
        return true;
    }

    public boolean cancel(String roomId) {
        Room r = rooms.get(roomId);
        if (r == null) return false;

        notifyAllObservers(RoomEvent.canceled(r));
        return true;
    }

    public Collection<Room> allRooms() {
        return Collections.unmodifiableCollection(rooms.values());
    }
}
