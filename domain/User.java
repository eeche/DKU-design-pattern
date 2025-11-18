package domain;

public class User {
    private final String userId;
    private final String userName;
    private final String phoneNumber;
    private final Tier tier;
    
    public User(String userId, String userName, String phoneNumber, Tier tier) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.tier = tier;
    }

    public String getUserId() {return userId;}
    public String getUserName() {return userName;}
    public String getPhoneNumber() {return phoneNumber;}
    public Tier getTier() {return tier;}
}
