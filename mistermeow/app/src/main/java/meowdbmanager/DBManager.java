package meowdbmanager;

import static com.mongodb.client.model.Projections.*;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

import meowindexer.Tokenizer.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class DBManager {
  private MongoClient mongoClient;
  private MongoDatabase DB;
  private MongoCollection<Document> invertedCollection;
  private MongoCollection<Document> docCollection;
  private MongoCollection<Document> queryCollection;

  public DBManager() {
    mongoClient = MongoClients.create("mongodb://localhost:27017");
    DB = mongoClient.getDatabase("meowDB");

    CreateCollectionOptions options = new CreateCollectionOptions().capped(false);
    DB.createCollection("Documents", options);
    DB.createCollection("InvertedIndex", options);
    DB.createCollection("Queries", options);

    invertedCollection = DB.getCollection("InvertedIndex");
    docCollection = DB.getCollection("Documents");
    queryCollection = DB.getCollection("Queries");
    invertedCollection.createIndex(new Document("token", 1));
    queryCollection.createIndex(new Document("query", 1));
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
      Bson projection = fields(include("hashedURL", "hashedDoc", "URL"), excludeId());

      // Find documents matching the filter in the docCollection
      FindIterable<Document> matchingUrls = docCollection.find(filter).projection(projection);

      // Convert the FindIterable to a list (may not be suitable for very large
      // datasets)
      List<Document> urlsList = (List<Document>) matchingUrls.into(new ArrayList<Document>());

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
      Bson filter = Filters.eq("inQueue", true);

      // Create a projection document to specify fields to retrieve
      Bson projection = fields(include("hashedURL", "hashedDoc", "URL", "ranker_id"), excludeId());

      // Find documents matching the filter in the docCollection
      FindIterable<Document> matchingUrls = docCollection.find(filter).projection(projection);

      // Convert the FindIterable to a list (may not be suitable for very large
      // datasets)
      List<Document> urlList = (List<Document>) matchingUrls.into(new ArrayList<Document>());

      return urlList;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
   * getUrlDocument - takes a url and returns its document.
   *
   * @param url - the url to search for in the database.
   * @return its document.
   */
  public String getUrlDocument(String url) {
    try {
      // Create a filter document to find documents with isInQueue = true
      Bson filter = Filters.eq("URL", url);

      // Find documents matching the filter in the docCollection
      FindIterable<Document> matchingUrls = docCollection.find(filter);

      return matchingUrls.first().getString("content");

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
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
      update.append("$set", new Document("inQueue", isInQueue));

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
   * takes the key of the parents array and append a new parent to it.
   *
   * @param key       - the key to search on.
   * @param value     - the value to match.
   * @param parent_id - the new parent id to add
   * @return boolean - indicating if the popularity incremented successfully or
   *         not.
   */
  public boolean updateParents(String key, String value, int parent_id) {
    try {
      UpdateResult updateResult = docCollection.updateOne(Filters.eq(key, value),
          Updates.push("parents", parent_id));

      // Update the document, returning true if successful
      return updateResult.getModifiedCount() == 1;

    } catch (MongoException e) {
      System.out.println("Error while updating Parents array: " + e.getMessage());
      return false;
    }
  }

  /**
   * getUrlsCount - Returns the number of Urls in the database.
   * 
   * @return int - represents number of Urls in the database.
   */
  public int getUrlsCount() {
    try {
      return (int) docCollection.countDocuments();

    } catch (MongoException e) {
      System.out.println("Error while getting urls count: " + e.getMessage());
      return -1;
    }
  }

  /**
   * getParentsArr - returns an array of parents for a certain url.
   * 
   * @param ranker_id - the url ranker id.
   * @return List<Integer> - list of parents ids
   */
  public List<Integer> getParentsArr(int ranker_id) {
    try {

      // Filter the Document I want.
      Document filter = new Document("ranker_id", ranker_id);

      // Find documents matching the filter in the docCollection
      FindIterable<Document> matchingUrls = docCollection.find(filter);

      return matchingUrls.first().getList("parents", Integer.class);

    } catch (MongoException e) {
      System.out.println("Error while getting parents array: " + e.getMessage());
      return new ArrayList<Integer>();
    }
  }

  public String insertDocument(String url, String title, String host,
      String content, String hashedUrl,
      String hashedDoc, int ranker_id, List<Integer> parents) {
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
          .append("indexed", false)
          .append("inQueue", true)
          .append("ranker_id", ranker_id)
          .append("parents", parents);

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
        Document result = invertedCollection.find(new Document("token", token)).first();
        if (result == null) {

          List<Document> docs = new ArrayList<>();
          docs.add(new Document("_id", new ObjectId(docID))
              .append("TF", tokens.get(token).count)
              .append("position", tokens.get(token).position));

          Document document = new Document().append("token", token).append("docs", docs);

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

  public boolean insertSuggestion(String query) {
    try {
      queryCollection.insertOne(new Document("query", query));
      return true;
    } catch (MongoException e) {
      if (e.getCode() == 11000)
        return true;
      System.out.println("Error occurred while insertion: " + e.getMessage());
      return false;
    }
  }

  public List<String> getSuggestions(String query, int limit) {
    List<String> matchingSuggestions = new ArrayList<>();

    try {

      Document docQuery = new Document("query", Pattern.compile("^" + Pattern.quote(query), Pattern.CASE_INSENSITIVE));
      queryCollection.find(docQuery).limit(limit).forEach(doc -> {
        matchingSuggestions.add(doc.getString("query"));
      });

      return matchingSuggestions;
    } catch (MongoException e) {

      System.out.println("Error occurred while getting suggestions: " +
          e.getMessage());
      return null;
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
      Document doc = docCollection.find(new Document("_id", new ObjectId(docID))).first();
      return doc;
    } catch (MongoException e) {

      System.out.println("Error occurred while getting document: " +
          e.getMessage());
      return null;
    }
  }

  public Document getInvertedIndex(String token) {
    try {
      Document indices = invertedCollection.find(new Document("token", token)).first();
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

  public List<ObjectId> getDocs(String[] tokens) {
    List<ObjectId> docIds = new ArrayList<>();
    List<String> tokenList = Arrays.asList(tokens);

    try {
      List<Document> pipeline = new ArrayList<>();
      pipeline.add(new Document("$match", new Document("token", new Document("$in", tokenList))));
      pipeline.add(new Document("$unwind", "$docs"));
      pipeline.add(new Document("$project", new Document("_id", "$docs._id")));

      List<Document> aggregationResult = invertedCollection.aggregate(pipeline).into(new ArrayList<>());
      for (Document doc : aggregationResult) {
        ObjectId docId = doc.getObjectId("_id");
        docIds.add(docId);
      }

      return docIds;
    } catch (MongoException e) {

      System.out.println("Error occurred while getting docs: " +
          e.getMessage());
      return null;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    mongoClient.close();
  }
}
