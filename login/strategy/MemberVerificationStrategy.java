package login.strategy;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import domain.Tier;
import domain.User;

public class MemberVerificationStrategy implements LoginStrategy {
    private static final Map<String, User> MEMBER_DB = Stream.of(
        new User("user123", "김정수", "010-1234-1234", Tier.PREMIUM),
        new User("user345", "이수빈", "010-5678-5678", Tier.PRO)
    ).collect(Collectors.toMap(User::getUserId, u -> u));

    @Override
    public User authenticate(String ... credentials) {
        if (credentials.length < 3) {
            System.err.println("인증에 필요한 정보가 부족합니다.");
            return null;
        }

        String userId = credentials[0];
        String userName = credentials[1];
        String phoneNumber = credentials[2];

        User user = MEMBER_DB.get(userId);

        if (user == null) {
            System.out.println("회원 아이디를 찾을 수 없습니다." + userId);
            return null;
        }

        // 이름과 전화번호가 모두 일치해야 인증 성공
        if (user.getUserName().equals(userName) && user.getPhoneNumber().equals(phoneNumber)) {
            return user;    // 인증 성공
        } else {
            System.out.println("입력 정보 불일치: 이름 또는 전화번호가 올바르지 않습니다.");
            return null;    // 인증 실패
        }
    }
}