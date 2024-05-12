package meowEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.*;

import org.bson.Document;
import org.bson.types.ObjectId;

import meowdbmanager.DBManager;

/*
 * get search query from frontend
 * tokenize and do all other necessary processing
 * search in the indexer
 * get all docs that match the query
 * use ranker + prioritize actual token over stemmed token
 * get pageination
 * return query tokens, numOfdocs, suggestions, search operation time
 * return snippets, url, website title, page title of every doc
 * 
 * separate functionalities:
 * 1. AND/OR/NOT
 * 2. Phrase search
 *
 * TODO:
 * 1. create the interface of the API
 * 2. get suggestions -> Endpoint (GET /suggestions)
 * 3. post search query -> Endpoint (POST /search)
 * 4. post pagination -> Endpoint (POST /page)
 * 6. get all results metadata -> middleware
 * 7. get all results info -> middleware
 * 8. preprocess the query -> middleware
 * 9. search in the indexer and get all docs -> middleware
 * 10. rank the docs -> middleware
 * 11. Save all docs in a list -> middleware
 *
 */

@Path("/")
public class queryEngine {
  private DBManager dbManager = new DBManager();
  private List<ObjectId> docs = new ArrayList<>();
  private String currentQuery = "";
  private boolean isPhraseMatching = false, isFirstTime = true;
  private String[] phrases = new String[3];
  private int[] operators = new int[2]; // 0: None, 1: AND, 2: OR, 3: NOT
  private final int numOfDocsInPage = 20;

  @GET
  @Path("/suggestions")
  public List<String> getSuggestions(
      @QueryParam("query") String query) {

    return dbManager.getSuggestions(query, 10);
  }

  @POST
  @Path("/search/{q}/{p}")
  public List<Document> searchQuery(
      @PathParam("q") String query,
      @PathParam("p") int page) {

    if (!query.equals(currentQuery))
      isFirstTime = true;

    if (isFirstTime) {
      parse(query);
      dbManager.insertSuggestion(query);
      docs = rankDocs(query.toLowerCase().split("\\s+"));
      isFirstTime = false;
      currentQuery = query;
    }

    int startIndex = page * numOfDocsInPage;
    int endIndex = Math.min(startIndex + numOfDocsInPage, docs.size());
    return getResults(docs.subList(startIndex, endIndex));
  }

  private void parse(String query) {
    isPhraseMatching = false;
    operators[0] = operators[1] = 0;
    phrases[0] = phrases[1] = phrases[2] = null;

    Matcher phraseMatch = Pattern.compile("\"[^\"]+\"").matcher(query);
    Matcher operatorMatch = Pattern.compile("\"\\s*(AND|OR|NOT)\\s*\"").matcher(query);

    int i = 0;
    while (phraseMatch.find()) {
      String phrase = phraseMatch.group().replaceAll("^\"|\"$", "").trim();
      System.out.println(phrase);
      phrases[i++] = phrase;
    }

    i = 0;
    while (operatorMatch.find()) {
      String operator = operatorMatch.group().replaceAll("^\"|\"$", "").trim();
      System.out.println(operator);
      operators[i++] = operator.equals("AND") ? 1
          : operator.equals("OR") ? 2
              : 3;
    }

    isPhraseMatching = phrases[0] != null;
    operators[0] = operators[1] = 0;
  }

  private List<Document> getResults(List<ObjectId> docs) {
    getResultsMetadata();
    getResultsInfo();
    return null;
  }

  private String getResultsMetadata() {
    return "Results Metadata";
  }

  private String getResultsInfo() {
    return "Results Info";
  }

  private String getSnippet(String doc, HashSet<String> tokens) {
    return "Snippet";
  }

  private List<ObjectId> rankDocs(String[] tokens) {
    return dbManager.getDocs(tokens);
  }
}
