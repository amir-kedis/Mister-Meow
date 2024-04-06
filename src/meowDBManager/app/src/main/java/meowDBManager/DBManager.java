package meowDBManager;

import com.mongodb.client.*;

public class DBManager {
  public static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
  MongoDatabase DB = mongoClient.getDatabase("meowDB");
}
