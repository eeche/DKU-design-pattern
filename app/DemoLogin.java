package app;

import login.LoginManager;
import login.strategy.LoginStrategy;
import login.strategy.MemberVerificationStrategy;

public class DemoLogin {
    public static void main(String[] args) {
        // 싱글톤 인스턴스 접근
        LoginManager loginManager = LoginManager.getInstance();

        LoginStrategy verificationStrategy = new MemberVerificationStrategy();

        System.out.println("--- 1. 로그인 시도 ---");
        boolean success1 = loginManager.login(
            verificationStrategy,
            "user123",
            "김정수",
            "010-1234-1234"
        );
        if (success1) {
            System.out.println("현재 로그인 사용자 ID: " + loginManager.getCurrentUser().getUserId());
            System.out.println("현재 사용자 등급: " + loginManager.getCurrentUser().getTier());
            loginManager.logout();
        }
        System.out.println("------------------------\n");

        System.out.println("--- 2. 로그인 시도 ---");
        loginManager.login(
            verificationStrategy,
            "user345", 
            "이수빈", 
            "010-9999-9999" // 전화번호 불일치
        );
        System.out.println("------------------------\n");
    }
}
