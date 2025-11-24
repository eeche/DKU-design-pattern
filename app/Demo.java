package app;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import domain.PaymentMethod;
import domain.Region;
import domain.Tier;

import login.LoginManager;
import login.strategy.LoginStrategy;
import login.strategy.MemberVerificationStrategy;

import observer.EmailNotifier;
import observer.ReservationSystem;
import observer.SMSNotifier;

import options.decorator.Bookable;
import options.decorator.CaffeAddon;
import options.decorator.LockerAddon;
import options.decorator.Room;

import payment.CardPaymentStrategy;

import pricing.PricingContext;
import pricing.PricingEngine;
import pricing.factory.RegionPricePolicyFactory;
import pricing.policy.PricePolicy;
import pricing.strategy.OptionsStrategy;
import pricing.strategy.PremiumTierStrategy;
import pricing.strategy.PricingStrategy;
import pricing.strategy.RegionPolicyStrategy;

public class Demo {

    public static void main(String[] args) {
        System.out.println("===== 공유오피스 통합 데모 시작 =====\n");

        // 1. 로그인 단계 (실패 1회 → 성공 1회)
        LoginManager loginManager = LoginManager.getInstance();
        LoginStrategy loginStrategy = new MemberVerificationStrategy();

        Tier userTier = runLoginFlow(loginManager, loginStrategy);
        if (userTier == null) {
            System.out.println("[DEMO] 로그인에 실패하여 이후 시나리오를 진행할 수 없습니다.");
            System.out.println("===== 데모 종료 =====");
            return;
        }

        System.out.println("\n[INFO] 로그인 성공. 등급 = " + userTier);
        System.out.println("----------------------------------------\n");

        // 2. 옵션 선택 + 가격 계산 + 결제
        //    (기존 pricingDemo 를 조정해서 사용)
        Room reservedRoom = new Room("A-101", 6, 20000); // 예약 대상 Room
        int hours = 5;                                   // 이용 시간

        BigDecimal totalPrice = runPricingAndPaymentFlow(userTier, reservedRoom, hours);

        // 3. 예약 생성 + 알림 발송 (Observer)
        runReservationObserverFlow(reservedRoom, totalPrice);

        // 마지막 정리
        if (loginManager.getCurrentUser() != null) {
            loginManager.logout();
        }

        System.out.println("\n===== 공유오피스 통합 데모 종료 =====");
    }

    
    // 로그인 시나리오:
    //  - 1차: 실패 (전화번호 불일치)
    //  - 2차: 성공
    // 성공 시 해당 사용자의 Tier를 리턴, 전부 실패하면 null 리턴.
    
    private static Tier runLoginFlow(LoginManager loginManager, LoginStrategy strategy) {
        System.out.println("===== 1. 로그인 데모 =====");

        System.out.println("--- [1차] 로그인 시도 (실패 케이스) ---");
        boolean first = loginManager.login(
            strategy,
            "user001",
            "홍길동",
            "010-9999-9999" // 의도적으로 틀린 번호
        );
        if (!first) {
            System.out.println("→ 1차 로그인 실패 (전화번호 불일치 등)\n");
        } else {
            System.out.println("→ 예상과 다르게 1차 로그인 성공\n");
            loginManager.logout();
        }

        System.out.println("--- [2차] 로그인 시도 (성공 케이스) ---");
        boolean second = loginManager.login(
            strategy,
            "user123",
            "김정수",
            "010-1234-1234" // 올바른 정보
        );
        if (!second) {
            System.out.println("→ 2차 로그인도 실패. 데모 진행 불가.\n");
            return null;
        }

        System.out.println("→ 2차 로그인 성공");
        System.out.println("   - 사용자 ID : " + loginManager.getCurrentUser().getUserId());
        System.out.println("   - 사용자 등급 : " + loginManager.getCurrentUser().getTier());

        return loginManager.getCurrentUser().getTier();
    }

    
    // 옵션 선택 → 가격 계산 → 결제까지 수행.
    //  - Region: SEOUL
    //  - Tier: 로그인 결과
    //  - Payment: CARD
    //  - Room + Decorator(카페, 사물함) + OptionsStrategy
    
    // @return 최종 결제 금액
    
    private static BigDecimal runPricingAndPaymentFlow(Tier tier, Room baseRoom, int hours) {
        System.out.println("===== 2. 옵션 선택 + 가격 계산 + 결제 데모 =====");

        // (1) 사용자 옵션 선택 (시나리오 고정)
        System.out.println("[옵션 선택] 기본 회의실: " + baseRoom.getRoomID()
                + " (최대 " + baseRoom.getCapacity() + "인, " + baseRoom.getBaseRatePerHour() + "원/시간)");
        System.out.println("[옵션 선택] 사용자 선택 옵션: 카페 이용 추가 + LARGE 사물함 1개");
        System.out.println("이용 시간: " + hours + "시간\n");

        // (2) Decorator 체인 구성
        Bookable optionChain = baseRoom;
        optionChain = new CaffeAddon(optionChain);
        optionChain = new LockerAddon(optionChain, "LARGE", 1);

        // (3) PricingContext 생성
        BigDecimal baseAmount = BigDecimal
                .valueOf(baseRoom.getBaseRatePerHour())
                .multiply(BigDecimal.valueOf(hours));
        Region region = Region.SEOUL;
        PaymentMethod method = PaymentMethod.CARD;

        PricingContext ctx = new PricingContext(baseAmount, tier, method);

        // (4) Region 정책 → Strategy
        PricePolicy regionPolicy = new RegionPricePolicyFactory().create(region);
        PricingStrategy regionStrategy = new RegionPolicyStrategy(regionPolicy);

        // (5) Tier 전략 (간단히 Premium 전략만 사용. 필요하면 Tier에 따라 분기 가능)
        PricingStrategy tierStrategy = new PremiumTierStrategy();

        // (6) OptionsStrategy (Decorator 옵션 비용을 Strategy 파이프라인에 합류)
        PricingStrategy optionsStrategy = new OptionsStrategy(optionChain, hours);

        // (7) 파이프라인 구성
        List<PricingStrategy> pipeline = new ArrayList<>();
        pipeline.add(regionStrategy);
        pipeline.add(tierStrategy);
        pipeline.add(optionsStrategy);

        PricingEngine engine = new PricingEngine(pipeline);
        BigDecimal total = engine.quote(ctx);

        System.out.println("[가격 계산 결과]");
        System.out.printf(" - Region: %s%n", region);
        System.out.printf(" - Tier  : %s%n", tier);
        System.out.printf(" - 옵션  : %s%n", optionChain.getDescription());
        System.out.printf(" - 최종 금액: %, .0f원%n%n", total.doubleValue());

        // (8) 결제 Strategy 적용 (CardPaymentStrategy)
        System.out.println("[결제 진행]");
        var paymentStrategy = new CardPaymentStrategy();
        paymentStrategy.pay(total);

        return total;
    }

    // 결제 완료 후 예약 생성 + Observer 알림 데모.
    private static void runReservationObserverFlow(Room room, BigDecimal paidAmount) {
        System.out.println("===== 3. 예약 + 알림(Observer) 데모 =====");

        ReservationSystem reservation = new ReservationSystem();

        // Observer 등록 (SMS + Email)
        reservation.addObserver(new SMSNotifier("010-1234-5678"));
        reservation.addObserver(new EmailNotifier("admin@room.com"));

        // 방 등록
        reservation.addRoom(room);

        System.out.printf("결제 완료 금액: %, .0f원 → 방 %s 예약 진행%n",
                paidAmount.doubleValue(), room.getRoomID());

        String userId = "user123";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        // 예약 시 알림 발송
        reservation.reserve(userId, room.getRoomID(), start, end);

        // 한 번 더 취소/재예약을 보여주고 싶으면:
        System.out.println("\n[추가 시나리오] 예약 취소 후 재예약");
        reservation.cancel(room.getRoomID());
        reservation.reserve(userId, room.getRoomID(), start, end);
    }
}
