package manager;

import logger.Logger;

public class SeatManagerDemo {

    public static void main(String[] args) {

        SeatManager sm = SeatManager.getInstance();

        // 좌석 등록
        sm.addSeat("A-01");
        sm.addSeat("A-02");
        sm.addSeat("B-01");

        // 좌석 이용
        sm.isAvailable("A-01");
        sm.occupy("A-01");
        sm.isAvailable("A-01");

        // 이미 점유된 좌석 점유 시도
        sm.occupy("A-01");

        // 좌석 비우기
        sm.free("A-01");

        // 전체 상태 조회
        sm.getAllSeatStatus();

        // ------- 로그 출력 -------
        System.out.println("\n=== Saved Logs ===");
        Logger.getInstance().getLogs().forEach(System.out::println);
    }
}
