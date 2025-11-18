package login.strategy;

import domain.User;

public interface LoginStrategy {
    User authenticate(String ... credentials); 
}
