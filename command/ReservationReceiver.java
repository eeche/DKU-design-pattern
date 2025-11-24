package command;

import domain.*;
import login.LoginManager;
import observer.ReservationSystem;
import options.decorator.*;
import payment.*;
import pricing.PricingContext;
import pricing.PricingEngine;
import pricing.factory.RegionPricePolicyFactory;
import pricing.policy.PricePolicy;
import pricing.strategy.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class ReservationReceiver {
    private final ReservationSystem reservationSystem;
    private final Map<String, List<Reservation>> bookingHistory = new HashMap<>();
    
    // [필수] 지역 정책 생성을 위한 팩토리
    private final RegionPricePolicyFactory regionFactory = new RegionPricePolicyFactory();

    public ReservationReceiver(ReservationSystem system) {
        this.reservationSystem = system;
    }

    // [핵심] 예약 + 옵션 + 가격책정 + 결제 + 알림을 한 번에 처리하는 메서드
    public void makeReservation(String roomId, Region region, List<String> options, PaymentMethod payMethod, 
                                LocalDateTime start, LocalDateTime end) {
        
        // 1. 로그인 체크
        User user = LoginManager.getInstance().getCurrentUser();
        if (user == null) {
            System.out.println("[오류] 로그인이 필요합니다.");
            return;
        }

        // 2. 방 확인 (중복 시간 체크 포함)
        if (!reservationSystem.isAvailable(roomId, start, end)) {
             System.out.println("[오류] 해당 시간에 예약이 불가능한 방입니다: " + roomId);
             return;
        }
        Room baseRoom = reservationSystem.getRoom(roomId);

        // 3. 데코레이터 패턴: 옵션 적용 (동적으로 객체 포장)
        Bookable roomWithOption = applyOptions(baseRoom, options);

        // 4. 전략 패턴: 가격 계산 (PricingEngine 가동)
        BigDecimal finalPrice = calculatePrice(user, region, roomWithOption, payMethod, start, end);
        
        System.out.println("\n--- 견적서 (" + user.getUserName() + "님) ---");
        System.out.println("상품: " + roomWithOption.getDescription());
        System.out.printf("지역: %s (지역 정책 적용)%n", region);
        System.out.printf("기간: %s ~ %s (%d시간)%n", start, end, getHours(start, end));
        System.out.printf("최종 결제 금액: %,.0f원%n", finalPrice);
        System.out.println("------------------------------------");

        // 5. 전략 패턴: 결제 진행 (PaymentStrategy)
        PaymentStrategy paymentStrategy = createPaymentStrategy(payMethod);
        paymentStrategy.pay(finalPrice); // 결제 로그 출력

        // 6. 예약 확정 (Observer 알림 발송)
        // 결제가 성공했으므로 실제 시스템에 예약 등록
        boolean success = reservationSystem.reserve(user.getUserId(), roomId, start, end);
        
        if (success) {
            // 내역 저장
            Reservation res = new Reservation(user.getUserId(), baseRoom, start, end); // DB 저장용은 원본 Room 참조
            bookingHistory.computeIfAbsent(user.getUserId(), k -> new ArrayList<>()).add(res);
            System.out.println("예약 및 결제 처리가 완료되었습니다.\n");
        }
    }

    // [헬퍼] 옵션 문자열을 실제 데코레이터 객체로 변환
    private Bookable applyOptions(Room room, List<String> options) {
        Bookable current = room;
        for (String opt : options) {
            switch (opt) {
                case "CAFE" -> current = new CaffeAddon(current);
                case "LOCKER" -> current = new LockerAddon(current, "MEDIUM", 1);
                case "MONITOR" -> current = new MonitorAddon(current);
                case "PRINTER" -> current = new PrinterAddon(current, "COLOR", 10);
            }
        }
        return current;
    }

    // [헬퍼] PricingEngine 구성 및 가격 계산
    private BigDecimal calculatePrice(User user, Region region, Bookable bookable, PaymentMethod method, LocalDateTime start, LocalDateTime end) {
        int hours = getHours(start, end);
        
        // Context 생성
        BigDecimal baseAmount = BigDecimal.valueOf(bookable.cost(hours)); // 데코레이터가 계산한 기본+옵션 요금
        PricingContext ctx = new PricingContext(baseAmount, user.getTier(), method);

        List<PricingStrategy> pipeline = new ArrayList<>();

        // (1) 지역 정책 추가
        PricePolicy policy = regionFactory.create(region);
        pipeline.add(new RegionPolicyStrategy(policy));

        // (2) 등급 정책 추가
        if (user.getTier() == Tier.PREMIUM) pipeline.add(new PremiumTierStrategy());
        else if (user.getTier() == Tier.PRO) pipeline.add(new ProTierStrategy());
        else pipeline.add(new BasicTierStrategy());

        // (3) 엔진 실행
        PricingEngine engine = new PricingEngine(pipeline);
        return engine.quote(ctx);
    }

    private PaymentStrategy createPaymentStrategy(PaymentMethod method) {
        return switch (method) {
            case CARD -> new CardPaymentStrategy();
            case CASH -> new CashPaymentStrategy();
            case TRANSFER -> new TransferPaymentStrategy();
        };
    }

    private int getHours(LocalDateTime start, LocalDateTime end) {
        long h = start.until(end, java.time.temporal.ChronoUnit.HOURS);
        return h < 1 ? 1 : (int) h;
    }

    // 취소 기능
    public void cancelBooking(String roomId) {
        if(reservationSystem.cancel(roomId)) {
             System.out.println("예약이 취소되고 환불 처리되었습니다.");
        }
    }

    // 조회 기능
    public void viewHistory() {
         User user = LoginManager.getInstance().getCurrentUser();
         if(user == null) return;
         List<Reservation> list = bookingHistory.getOrDefault(user.getUserId(), Collections.emptyList());
         System.out.println(user.getUserName() + "님의 예약 목록:");
         if (list.isEmpty()) System.out.println("(예약 없음)");
         else list.forEach(System.out::println);
    }
}