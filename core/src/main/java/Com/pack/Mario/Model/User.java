package Com.pack.Mario.Model;

public class User {
    String username;
    String email;
    String password;
    String salt;
    int point;
    int level;
    int dobDay;
    int dobMonth;
    int dobYear;

    public User(String email, String password, String salt, int point, int level, int dobDay,
                int dobMonth,
                int dobYear) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.point = point;
        this.level = level;
        this.dobDay = dobDay;
        this.dobMonth = dobMonth;
        this.dobYear = dobYear;

    }

    public User() {

    }

    public String getEmail() {
        return email;

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;

    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDobDay() {
        return dobDay;
    }

    public void setDobDay(int dobDay) {
        this.dobDay = dobDay;
    }

    public int getDobMonth() {
        return dobMonth;
    }

    public void setDobMonth(int dobMonth) {
        this.dobMonth = dobMonth;
    }

    public int getDobYear() {
        return dobYear;
    }

    public void setDobYear(int dobYear) {
        this.dobYear = dobYear;
    }

}
