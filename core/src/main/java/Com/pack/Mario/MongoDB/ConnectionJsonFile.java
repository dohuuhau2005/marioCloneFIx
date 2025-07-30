//package Com.pack.Mario.MongoDB;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//
//public class ConnectionFactory {
//    static MongoClient mongoClient;
//    static MongoDatabase db;
//
//    public static MongoDatabase getMongoClient() {
//        if (mongoClient == null) {
//            mongoClient = MongoClients.create("mongodb+srv://nhom79app:soZjDRCdtBqKhtec@supermario.ykczafv.mongodb.net/");
//            db = mongoClient.getDatabase("supermario");
//        }
//        return db;
//
//    }
//}

package Com.pack.Mario.MongoDB;

import Com.pack.Mario.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConnectionJsonFile {
    private final String FILE_PATH = "game.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Map<String, User> readUsersFromFile() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type type = new TypeToken<Map<String, User>>() {
            }.getType();
            Map<String, User> result = gson.fromJson(reader, type);
            return result != null ? result : new HashMap<>();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    public void writeUsersToFile(Map<String, User> users) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



