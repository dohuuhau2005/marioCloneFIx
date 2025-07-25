package Com.pack.Mario.Model;

import Com.pack.Mario.MongoDB.ConnectionFactory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;


public class UserDao {
    User user;

    public UserDao() {
    }

    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public void Insert(User user) {
        MongoDatabase db = ConnectionFactory.getMongoClient();
        try {
            MongoCollection<Document> collection = db.getCollection("Users");
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            user.setSalt(generateSalt(16));

            String passwordWithSalt = user.getPassword() + user.getSalt();
            byte[] hashedBytes = md.digest(passwordWithSalt.getBytes());

            String hashPassword = Base64.getEncoder().encodeToString(hashedBytes);

            Document Profile = new Document("Username", user.getUsername())
                .append("Password", hashPassword)
                .append("Salt", user.getSalt())
                .append("Day of Birth", user.getDobDay()).append("Month of Birth", user.getDobMonth()).append("Year of Birth", user.getDobYear());
            Document Info = new Document("Email", user.getEmail())
                .append("Profile", Profile);
            collection.insertOne(Info);
            System.out.println("Inserted User: " + user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();


        }

    }

    //check username or email have been existed
    public boolean CheckUser(String username, String email) {
        MongoDatabase db = ConnectionFactory.getMongoClient();
        try {
            MongoCollection<Document> collection = db.getCollection("Users");
            Document query = new Document("Email", email);
            Document resultEmail = collection.find(query).first();
            Document query2 = new Document("Profile.Username", username);
            Document resultUsername = collection.find(query2).first();
            if (resultEmail != null || resultUsername != null) {
                return true;
            }
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException();
        }
        return false;
    }


    public boolean Login(String email, String password) {
        MongoDatabase db = ConnectionFactory.getMongoClient();
        try {
            MongoCollection<Document> collection = db.getCollection("Users");
            Document query = new Document("Email", email);
            Document resultEmail = collection.find(query).first();
            if (resultEmail != null) {
                Document profile = (Document) resultEmail.get("Profile");
                String passwordDB = profile.getString("Password");
                String salt = profile.getString("Salt");
                String PasswordWithSalt = password + salt;
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte[] hashedBytes = md.digest(PasswordWithSalt.getBytes());

                String hashPassword = Base64.getEncoder().encodeToString(hashedBytes);
                if (passwordDB.equals(hashPassword)) {
                    return true;
                } else
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return false;
    }

    public User GetUser(String email) {
        MongoDatabase db = ConnectionFactory.getMongoClient();
        try {
            MongoCollection<Document> collection = db.getCollection("Users");
            Document query = new Document("Email", email);
            Document resultEmail = collection.find(query).first();
            if (resultEmail != null) {
                Document profile = (Document) resultEmail.get("Profile");
                String Username = profile.getString("Username");
                Integer DayOfBirth = profile.getInteger("Day of Birth");
                Integer MonthOfBirth = profile.getInteger("Month of Birth");
                Integer YearOfBirth = profile.getInteger("Year of Birth");
                user = new User();
                user.setUsername(Username);
                user.setDobDay(DayOfBirth);
                user.setDobMonth(MonthOfBirth);
                user.setDobYear(YearOfBirth);
                return user;

            } else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("khong tìm thấy user");
        }
        return null;
    }

    public void ChangePassword(String email, String newPassword) {
        MongoDatabase db = ConnectionFactory.getMongoClient();
        try {
            MongoCollection<Document> collection = db.getCollection("Users");
            Document query = new Document("Email", email);
            Document resultEmail = collection.find(query).first();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String salt = generateSalt(16);

            String passwordWithSalt = newPassword + salt;
            byte[] hashedBytes = md.digest(passwordWithSalt.getBytes());

            String hashPassword = Base64.getEncoder().encodeToString(hashedBytes);
            Document updatePass = new Document()
                .append("Profile.Password", hashPassword)
                .append("Profile.Salt", salt);
            Document updateOperation = new Document("$set", updatePass);
            collection.updateOne(query, updateOperation);
            System.out.println("Password updated successfully for: " + email);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error changing password");
        }
    }
}
