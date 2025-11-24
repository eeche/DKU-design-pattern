package app;

import command.*;
import domain.PaymentMethod;
import domain.Region;
import login.LoginManager;
import login.strategy.MemberVerificationStrategy;
import observer.*;
import options.decorator.Room;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DemoCommand {
    public static void main(String[] args) {
        System.out.println("========== [공유오피스 통합 시스템] ==========\n");

        // 1. 초기 설정
        ReservationSystem system = new ReservationSystem();
        system.addObserver(new SMSNotifier("010-ADMIN-0000")); // 알림 등록
        
        system.addRoom(new Room("305", 4, 10000));  // 305호 (1만원/시간)
        system.addRoom(new Room("406", 8, 20000));  // 406호 (2만원/시간)

        ReservationReceiver receiver = new ReservationReceiver(system);
        CommandInvoker invoker = new CommandInvoker();
        LoginManager loginManager = LoginManager.getInstance();
        MemberVerificationStrategy verify = new MemberVerificationStrategy();


        // [시나리오 1] 김정수(BASIC) - 305호 예약 (옵션: 카페, 모니터 / 결제: 카드)
        System.out.println("\n>>> [User 1] 김정수 로그인 & 예약 시도");
        loginManager.login(verify, "user123", "김정수", "010-1234-1234");

        LocalDateTime time1_Start = LocalDateTime.of(2025, 12, 1, 14, 0);
        LocalDateTime time1_End   = LocalDateTime.of(2025, 12, 1, 17, 0); // 3시간

        // 커맨드 생성: "305호를 14~17시에 카페랑 모니터 추가해서 카드로 결제"
        Command bookCmd1 = new BookCommand(
            receiver, "305", 
            Region.SEOUL,
            Arrays.asList("CAFE", "MONITOR"), 
            PaymentMethod.CARD, 
            time1_Start, time1_End
        );

        invoker.setCommand(bookCmd1);
        invoker.invoke(); // -> 가격계산 -> 카드결제 -> 예약확정 -> 알림발송 일괄 처리됨

        // 예약 취소
        System.out.println("\n>>> [User 1] 김정수 변심으로 예약 취소");
        invoker.setCommand(new CancelCommand(receiver, "305"));
        invoker.invoke();




        // [시나리오 2] 이수빈(PRO) - 406호 예약 (옵션: 없음 / 결제: 계좌이체)
        System.out.println("\n>>> [User 2] 이수빈 로그인 & 예약 시도");
        loginManager.login(verify, "user345", "이수빈", "010-5678-5678");

        LocalDateTime time2_Start = LocalDateTime.of(2025, 12, 1, 14, 0);
        LocalDateTime time2_End   = LocalDateTime.of(2025, 12, 1, 16, 0); // 2시간

        Command bookCmd2 = new BookCommand(
            receiver, "406", 
            Region.JEJU,
            List.of("LOCKER"), // 옵션 없음
            PaymentMethod.TRANSFER, 
            time2_Start, time2_End
        );
        invoker.setCommand(bookCmd2);
        invoker.invoke(); // -> PRO 등급 할인 적용 -> 계좌이체 -> 예약확정 -> 알림발송



    
        // [시나리오 3] 박민주(PREMIUM) - 406호 중복 예약 시도
        System.out.println("\n>>> [User 3] 박민주 로그인 시도");
        loginManager.login(verify, "user567", "박민주", "010-9999-9999");

        System.out.println("\n>>> [User 3] 박민주 로그인 재시도");
        loginManager.login(verify, "user567", "박민주", "010-3333-3333");

        // 이수빈이 예약한 14~16시와 겹치는 시간 요청 (15~17시)
        LocalDateTime time3_Start = LocalDateTime.of(2025, 12, 1, 15, 0);
        LocalDateTime time3_End   = LocalDateTime.of(2025, 12, 1, 17, 0);

        Command bookCmd3 = new BookCommand(
            receiver, "406",
            Region.JEJU,
            List.of(),
            PaymentMethod.CASH, 
            time3_Start, time3_End
        );
        invoker.setCommand(bookCmd3);
        invoker.invoke(); // -> "예약 불가능" 메시지 출력되고 결제/예약 진행 안 됨
        
        System.out.println("\n========== [데모 종료] ==========");
    }
}
