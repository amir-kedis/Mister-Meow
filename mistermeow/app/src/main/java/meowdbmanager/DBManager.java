package src.main.java.meowdbmanager;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import java.net.*;
import org.bson.Document;

public class DBManager {
  public MongoClient mongoClient;
  public MongoDatabase DB;

  public DBManager() {
    mongoClient = MongoClients.create("mongodb://localhost:27017");
    DB = mongoClient.getDatabase("meowDB");

    CreateCollectionOptions options =
        new CreateCollectionOptions().capped(false);
    DB.createCollection("Documents", options);
    DB.createCollection("InvertedIndex", options);
  }

  public String insertDocument(String url, String title, String host,
                               String content) {
    try {
      // Check for valid URL
      new URL(url).toURI();

      MongoCollection<Document> docCollection = DB.getCollection("Documents");
      Document document = new Document()
                              .append("URL", url)
                              .append("title", title)
                              .append("host", host)
                              .append("content", content);

      String insertedId =
          docCollection.insertOne(document).getInsertedId().toString();
      return insertedId;

    } catch (MalformedURLException | URISyntaxException e) {

      System.out.println("Error with URL: " + e.getMessage());
      return null;

    } catch (MongoException e) {

      System.out.println("Error occurred while insertion: " + e.getMessage());
      return null;
    }
  }

  public String insertInverted(String docID, String[] token) { return null; }
}
