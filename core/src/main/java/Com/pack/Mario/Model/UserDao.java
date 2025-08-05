package Com.pack.Mario.Model;

import Com.pack.Mario.MongoDB.ConnectionJsonFile;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class UserDao {
    private final ConnectionJsonFile fileStorage = new ConnectionJsonFile();
    private final Map<String, User> users;

    public UserDao() {
        System.out.println("Đang đọc file user.json...");
        this.users = fileStorage.readUsersFromFile();
        System.out.println("Đã đọc xong user.json, tổng số user: " + users.size());
    }

    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public void Insert(User user) {
        try {
            if (CheckUser(user.getUsername(), user.getEmail())) {
                System.out.println("User already exists");
                return;
            }

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            user.setSalt(generateSalt(16));

            String passwordWithSalt = user.getPassword() + user.getSalt();
            byte[] hashedBytes = md.digest(passwordWithSalt.getBytes());
            String hashPassword = Base64.getEncoder().encodeToString(hashedBytes);

            user.setPassword(hashPassword);
            users.put(user.getEmail(), user);
            fileStorage.writeUsersToFile(users);
            System.out.println("Inserted User: " + user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi insert user: " + e.getMessage());
            throw new RuntimeException("Error inserting user");
        }
    }

    public boolean CheckUser(String username, String email) {
        return users.values().stream().anyMatch(u -> u.getUsername().equals(username) || u.getEmail().equals(email));
    }

    public void UpdateScore(String email, int score) {
        try {
            User user = users.get(email);
            if (user == null) {
                System.out.println("User not found");
            }
            user.setPoint(score);
            users.put(email, user);
            fileStorage.writeUsersToFile(users);
            System.out.println("Update score: " + score);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in updating score");
        }
    }

    public boolean Login(String email, String password) {
        try {
            User user = users.get(email);
            if (user == null) return false;

            String passwordWithSalt = password + user.getSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest(passwordWithSalt.getBytes());
            String hashPassword = Base64.getEncoder().encodeToString(hashedBytes);

            System.out.println("Email: " + email);
            System.out.println("Raw password: " + password);
            System.out.println("Salt: " + user.getSalt());
            System.out.println("Stored password: " + user.getPassword());
            System.out.println("Computed hash: " + hashPassword);

            return user.getPassword().equals(hashPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error during login");
        }
    }

    public User GetUser(String email) {
        User user = users.get(email);
        if (user == null) {
            System.out.println("User not found");
        }
        return user;
    }

    public void ChangePassword(String email, String newPassword) {
        try {
            User user = users.get(email);
            if (user == null) {
                System.out.println("User not found");
                return;
            }
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String salt = generateSalt(16);
            String passwordWithSalt = newPassword + salt;
            byte[] hashedBytes = md.digest(passwordWithSalt.getBytes());
            String hashPassword = Base64.getEncoder().encodeToString(hashedBytes);

            user.setSalt(salt);
            user.setPassword(hashPassword);
            users.put(email, user);
            fileStorage.writeUsersToFile(users);
            System.out.println("Password updated successfully for: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error changing password");
        }
    }

    public Boolean ChangeProfile(String email, String username, int day, int month, int year) {
        User user = users.get(email);
        if (user == null) return false;
        try {
            user.setUsername(username);
            user.setDobDay(day);
            user.setDobMonth(month);
            user.setDobYear(year);
            users.put(email, user);
            fileStorage.writeUsersToFile(users);
            System.out.println("Profile updated successfully for: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error changing profile" + e.getMessage());
            throw new RuntimeException("Error changing profile");

        }
        return false;
    }

    public User[] RankingUsers() {
        try {
            if (users == null || users.isEmpty()) return new User[0];

            List<User> sortedUsers = new ArrayList<>(users.values());

            User[] usersArray = new User[sortedUsers.size()];
            for (int i = 0; i < sortedUsers.size(); i++) {
                usersArray[i] = sortedUsers.get(i);
            }


            for (int i = 0; i < usersArray.length - 1; i++) {
                for (int j = i + 1; j < usersArray.length; j++) {
                    if (usersArray[i].getLevel() < usersArray[j].getLevel()
                        || (usersArray[j].getLevel() == usersArray[i].getLevel() && usersArray[i].getPoint() < usersArray[j].getPoint())) {
                        User tmp = usersArray[i];
                        usersArray[i] = usersArray[j];
                        usersArray[j] = tmp;
                    }

                }
            }
//            // Sắp xếp giảm dần theo điểm
//            sortedUsers.sort((u1, u2) -> Integer.compare(u2.getLevel(), u1.getLevel()));


            return usersArray;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error ranking users" + e.getMessage());
            throw new RuntimeException("Error ranking users");
        }


    }

    public void upDateLevel(String email, int level, int point) {
        try {
            User user = users.get(email);
            if (user == null) return;
            user.setLevel(level);
            user.setPoint(point);
            users.put(email, user);
            fileStorage.writeUsersToFile(users);
            System.out.println("Update level successfully for: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in updating level");
        }
    }

    public int getLevel(String email) {
        try {
            User user = users.get(email);
            if (user == null) return -1;
            return user.getLevel();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
