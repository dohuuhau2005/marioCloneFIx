package Com.pack.Mario.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConnectionFactory {
    static MongoClient mongoClient;
    static MongoDatabase db;

    public static MongoDatabase getMongoClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb+srv://nhom79app:soZjDRCdtBqKhtec@supermario.ykczafv.mongodb.net/");
            db = mongoClient.getDatabase("supermario");
        }
        return db;

    }
}
