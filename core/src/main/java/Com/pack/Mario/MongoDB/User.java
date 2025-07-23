package Com.pack.Mario.MongoDB;

public class User {
    String email;
    String password;
    String salt;
    int point;
    int level;
    String DOB;

    public User(String email, String password, String salt, int point, int level, String DOB) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.point = point;
        this.level = level;
        this.DOB = DOB;

    }

    public String getEmail() {
        return email;

    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

}
