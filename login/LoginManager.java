package login;

import login.strategy.LoginStrategy;
import domain.User;

public final class LoginManager {
    private static final LoginManager INSTANCE = new LoginManager();
    private User currentUser;

    private LoginManager() {}

    public static LoginManager getInstance() {
        return INSTANCE;
    }

    public boolean login(LoginStrategy strategy, String ... credentials) {
        User user = strategy.authenticate(credentials);
        if (user != null) {
            this.currentUser = user;
            System.out.printf("로그인 성공: %s (%s 등급)%n", user.getUserName(), user.getTier());
            return true;
        } else {
            return false;
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
        System.out.println("로그아웃");
    }
}
