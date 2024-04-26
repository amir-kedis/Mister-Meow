package meowdbmanager;

import static com.mongodb.client.model.Projections.*;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.net.*;
import java.util.*;
import meowindexer.Tokenizer.*;
import org.bson.Document;
import org.bson.conversions.Bson;
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
    invertedCollection.createIndex(new Document("token", 1));
  }

  /**
   * retrieveHashedDataOfUrls - returns the hashedUrl, hashedDoc and URL of each
   * url stored in the database.
   *
   * @return List<Document> - a list of documents (urls) that are in the queue.
   */
  public List<Document> retrieveHashedDataOfUrls() {
    try {
      // Empty filter.
      Bson filter = Filters.empty();

      // Create a projection document to specify fields to retrieve
      Bson projection =
          fields(include("hashedURL", "hashedDoc", "URL"), excludeId());

      // Find documents matching the filter in the docCollection
      FindIterable<Document> matchingUrls =
          docCollection.find(filter).projection(projection);

      // Convert the FindIterable to a list (may not be suitable for very large
      // datasets)
      List<Document> urlsList =
          (List<Document>)matchingUrls.into(new ArrayList<Document>());

      return urlsList;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
   * retrieveUrlsInQueue - gets any url that was in the queue when crawler
   * stopped.
   *
   * @return List<Document> - a list of documents (urls) that were in the queue.
   */
  public List<Document> retrieveUrlsInQueue() {
    try {
      // Create a filter document to find documents with isInQueue = true
      Bson filter = Filters.eq("isInQueue", true);

      // Find documents matching the filter in the docCollection
      FindIterable<Document> matchingUrls = docCollection.find(filter);

      // Convert the FindIterable to a list (may not be suitable for very large
      // datasets)
      List<Document> urlList =
          (List<Document>)matchingUrls.into(new ArrayList<Document>());

      return urlList;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
   * updateInQueue - takes a url and updates its inQueue state by a boolean.
   *
   * @param url       - the url of the website.
   * @param isInQueue - the state of the url.
   * @return boolean - indicating if the update operation worked or not.
   */
  public boolean updateInQueue(String url, boolean isInQueue) {
    try {
      // Build a filter to find the document with the specified key & value.
      Document filter = new Document("URL", url);

      // Create an update document to set the "isInQueue" field
      Document update = new Document();
      update.put("inQueue", isInQueue);

      // Update the document, returning true if successful
      UpdateResult updateResult = docCollection.updateOne(filter, update);
      return updateResult.getModifiedCount() == 1;

    } catch (MongoException e) {
      System.out.println(
          "Error while updating the inQueue state of the url.\n" +
          e.getMessage());
      return false;
    }
  }

  /**
   * takes a value of a certain key and increments the popularity of it.
   *
   * @param key   - the key to search on.
   * @param value - the value to match.
   * @return boolean - indicating if the popularity incremented successfuly or
   *         not.
   */
  public boolean incrementPopularity(String key, String value) {
    try {
      // Build a filter to find the document with the specified key & value.
      Document filter = new Document(key, value);

      // Update by incrementing the "popularity" field (atomic operation)
      Document update = new Document("$inc", new Document("popularity", 1));

      // Update the document, returning true if successful
      UpdateResult updateResult = docCollection.updateOne(filter, update);
      return updateResult.getModifiedCount() == 1;

    } catch (MongoException e) {
      System.out.println("Error while updating popularity: " + e.getMessage());
      return false;
    }
  }

  public String insertDocument(String url, String title, String host,
                               String content, String hashedUrl,
                               String hashedDoc) {
    try {
      // Check for valid URL
      new URL(url).toURI();

      Document document = new Document()
                              .append("URL", url)
                              .append("title", title)
                              .append("host", host)
                              .append("content", content)
                              .append("hashedURL", hashedUrl)
                              .append("hashedDoc", hashedDoc)
                              .append("popularity", 1)
                              .append("indexed", false)
                              .append("inQueue", true);

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

          Document document =
              new Document().append("token", token).append("docs", docs);

          invertedCollection.insertOne(document);
        } else {

          Document newDoc = new Document("_id", new ObjectId(docID))
                                .append("TF", tokens.get(token).count)
                                .append("position", tokens.get(token).position);

          invertedCollection.updateOne(Filters.eq("token", token),
                                       Updates.push("docs", newDoc));
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
