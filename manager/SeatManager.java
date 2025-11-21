package manager;

import logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class SeatManager {

    // ---- Singleton ----
    private static final SeatManager INSTANCE = new SeatManager();

    private SeatManager() {}
    public static SeatManager getInstance() {
        return INSTANCE;
    }
    // -------------------

    // seatId → 사용 여부(true=사용중, false=비어있음)
    private final Map<String, Boolean> seatStatus = new HashMap<>();


    /**
     * 좌석 등록
     */
    public void addSeat(String seatId) {
        seatStatus.putIfAbsent(seatId, false);
        Logger.getInstance().info("[좌석등록] " + seatId + " 등록됨");
    }

    /**
     * 좌석 사용 가능한지 확인
     */
    public boolean isAvailable(String seatId) {
        boolean available = !seatStatus.getOrDefault(seatId, false);
        Logger.getInstance().info("[좌석조회] " + seatId + " 사용 가능? → " + available);
        return available;
    }

    /**
     * 좌석 점유
     */
    public boolean occupy(String seatId) {
        if (!seatStatus.containsKey(seatId)) {
            Logger.getInstance().warn("[점유실패] 존재하지 않는 좌석: " + seatId);
            throw new IllegalArgumentException("존재하지 않는 좌석: " + seatId);
        }

        if (seatStatus.get(seatId)) {
            Logger.getInstance().warn("[점유실패] 이미 사용 중: " + seatId);
            return false;
        }

        seatStatus.put(seatId, true);
        Logger.getInstance().info("[점유성공] " + seatId + " 사용 시작");
        return true;
    }

    /**
     * 좌석 비우기
     */
    public void free(String seatId) {
        if (!seatStatus.containsKey(seatId)) {
            Logger.getInstance().warn("[비우기실패] 존재하지 않는 좌석: " + seatId);
            throw new IllegalArgumentException("존재하지 않는 좌석: " + seatId);
        }

        seatStatus.put(seatId, false);
        Logger.getInstance().info("[좌석비움] " + seatId + " 사용 종료");
    }

    /**
     * 전체 좌석 상태 반환
     */
    public Map<String, Boolean> getAllSeatStatus() {
        Logger.getInstance().info("[전체조회] 좌석 전체 상태 조회");
        return seatStatus;
    }
}
