package meowdbmanager;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import java.net.*;
import java.util.*;
import meowindexer.Tokenizer.*;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DBManager {
  private MongoClient mongoClient;
  private MongoDatabase DB;
  private MongoCollection<Document> invertedCollection;
  private MongoCollection<Document> docCollection;

  public DBManager() {
    mongoClient = MongoClients.create("mongodb://localhost:27017");
    DB = mongoClient.getDatabase("meowDB");

    CreateCollectionOptions options =
        new CreateCollectionOptions().capped(false);
    DB.createCollection("Documents", options);
    DB.createCollection("InvertedIndex", options);

    invertedCollection = DB.getCollection("InvertedIndex");
    docCollection = DB.getCollection("Documents");
  }

  public String insertDocument(String url, String title, String host,
                               String content) {
    try {
      // Check for valid URL
      new URL(url).toURI();

      Document document = new Document()
                              .append("URL", url)
                              .append("title", title)
                              .append("host", host)
                              .append("content", content)
                              .append("indexed", false);

      String insertedId = docCollection.insertOne(document)
                              .getInsertedId()
                              .asObjectId()
                              .getValue()
                              .toString();
      return insertedId;

    } catch (MalformedURLException | URISyntaxException e) {

      System.out.println("Error with URL: " + e.getMessage());
      return null;

    } catch (MongoException e) {

      System.out.println("Error occurred while insertion: " + e.getMessage());
      return null;
    }
  }

  public boolean insertInverted(String docID, HashMap<String, Token> tokens) {
    try {

      for (String token : tokens.keySet()) {

        Document result =
            invertedCollection.find(new Document("token", token)).first();
        if (result == null) {

          List<Document> docs = new ArrayList<>();
          docs.add(new Document("_id", new ObjectId(docID))
                       .append("TF", tokens.get(token).count)
                       .append("position", tokens.get(token).position));

          Document document = new Document("_id", new ObjectId())
                                  .append("token", token)
                                  .append("docs", docs);

          invertedCollection.insertOne(document);
        } else {

          Document update = new Document(
              "$pull",
              new Document("docs", new Document("_id", new ObjectId(docID))));
          invertedCollection.updateOne(new Document("token", token), update);

          List<Document> docs = result.getList("docs", Document.class);
          docs.add(new Document("_id", new ObjectId(docID))
                       .append("TF", tokens.get(token).count)
                       .append("position", tokens.get(token).position));

          update = new Document("$set", new Document("docs", docs));
          invertedCollection.updateOne(new Document("token", token), update);
        }
      }

      // update document to be indexed
      Document update = new Document("$set", new Document("indexed", true));
      docCollection.updateOne(new Document("_id", new ObjectId(docID)), update);
      return true;
    } catch (MongoException e) {

      System.out.println("Error occurred while insertion: " + e.getMessage());
      return false;
    }
  }

  public List<Document> getDocumentsNotIndexed(int limit) {
    try {
      List<Document> docs = new ArrayList<>();
      Document query = new Document("indexed", false);

      docs = docCollection.find(query).limit(limit).into(new ArrayList<>());

      return docs;
    } catch (MongoException e) {

      System.out.println("Error occurred while getting documents: " +
                         e.getMessage());
      return null;
    }
  }

  public Document getDocument(String docID) {
    try {
      Document doc =
          docCollection.find(new Document("_id", new ObjectId(docID))).first();
      return doc;
    } catch (MongoException e) {

      System.out.println("Error occurred while getting document: " +
                         e.getMessage());
      return null;
    }
  }

  public Document getInvertedIndex(String token) {
    try {
      Document indices =
          invertedCollection.find(new Document("token", token)).first();
      if (indices == null)
        return null;

      int DF = indices.getList("docs", Document.class).size();
      indices.append("DF", DF);
      indices.remove("_id");

      return indices;
    } catch (MongoException e) {

      System.out.println("Error occurred while getting indices: " +
                         e.getMessage());
      return null;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    mongoClient.close();
  }
}
