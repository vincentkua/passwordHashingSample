package vk.loginsample.models;

public class User {
    private Integer id;
    private String userName;
    private String hashedPassword;

    public User() {
    }

    public User(Integer id, String userName, String hashedPassword) {
        this.id = id;
        this.userName = userName;
        this.hashedPassword = hashedPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", hashedPassword=" + hashedPassword + "]";
    }
}
