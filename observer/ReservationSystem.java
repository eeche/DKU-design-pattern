package observer;

import domain.Reservation;
import domain.RoomStatus;
import options.decorator.Room;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ReservationSystem {
private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final List<RoomObserver> observers = new ArrayList<>();

    // [핵심] 방 ID별 예약 리스트를 관리 (시간 겹침 확인용)
    private final Map<String, List<Reservation>> roomReservations = new ConcurrentHashMap<>();

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

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    // -----------------------------------------------------------
    // [수정된 부분] 4개 인자를 받는 reserve 메서드 추가
    // -----------------------------------------------------------
    public boolean reserve(String userId, String roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        Room room = rooms.get(roomId);
        if (room == null) {
            System.out.println("[시스템] 존재하지 않는 방: " + roomId);
            return false;
        }

        // 해당 방의 기존 예약 리스트 가져오기
        List<Reservation> existingReservations = roomReservations.getOrDefault(roomId, new ArrayList<>());

        // [중복 검사] 기존 예약 중 하나라도 시간이 겹치면 실패 (취소된 건 제외)
        boolean isOverlapped = existingReservations.stream()
            .filter(r -> r.getStatus() != RoomStatus.CANCELED)
            .anyMatch(r -> r.isOverlapping(checkIn, checkOut));

        if (isOverlapped) {
            System.out.println("[시스템] 예약 실패: 해당 시간에 이미 예약이 존재합니다. (" + roomId + ")");
            return false;
        }

        // 예약 성공 처리: 리스트에 추가
        Reservation newReservation = new Reservation(userId, room, checkIn, checkOut);
        roomReservations.computeIfAbsent(roomId, k -> new ArrayList<>()).add(newReservation);
        
        // 알림 발송
        notifyAllObservers(RoomEvent.reserved(room));
        return true;
    }

    // [추가] 예약 가능 여부 미리 확인 메서드 (ReservationReceiver에서 사용)
    public boolean isAvailable(String roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        if (!rooms.containsKey(roomId)) return false;
        
        List<Reservation> list = roomReservations.getOrDefault(roomId, new ArrayList<>());
        return list.stream()
                .filter(r -> r.getStatus() != RoomStatus.CANCELED)
                .noneMatch(r -> r.isOverlapping(checkIn, checkOut));
    }

    public boolean cancel(String roomId) {
        List<Reservation> list = roomReservations.get(roomId);
        if (list == null || list.isEmpty()) return false;

        // 가장 최근의 활성 예약을 찾아 취소
        Optional<Reservation> lastActive = list.stream()
                .filter(r -> r.getStatus() != RoomStatus.CANCELED)
                .reduce((first, second) -> second); // 마지막 요소

        if (lastActive.isPresent()) {
            lastActive.get().setStatus(RoomStatus.CANCELED);
            notifyAllObservers(RoomEvent.canceled(rooms.get(roomId)));
            return true;
        }
        return false;
    }
}
