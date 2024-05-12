package meowEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.ws.rs.*;
import meowdbmanager.DBManager;
import meowindexer.Tokenizer;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;

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
  private boolean isPhraseMatching = false, isFirstTime = true;
  private int[] operators = new int[2]; // 0: None, 1: AND, 2: OR, 3: NOT
  private int windowCharSize = 100;

  @GET
  @Path("/suggestions")
  public List<String> getSuggestions(@QueryParam("query") String query) {

    return dbManager.getSuggestions(query, 10);
  }

  @POST
  @Path("/search/{q}/{p}")
  public List<Document> searchQuery(@PathParam("q") String query,
      @PathParam("p") int page) {

    if (isFirstTime) {
      parse(query);
      dbManager.insertSuggestion(query);
      docs = rankDocs(query.toLowerCase().split("\\s+"));
      isFirstTime = false;
    }

    int startIndex = page * 20;
    int endIndex = Math.min(startIndex + 20, docs.size());
    return getResults(docs.subList(startIndex, endIndex));
  }

  private void parse(String query) {
    isPhraseMatching = false;
    operators[0] = operators[1] = 0;
    String operatorString[] = { "AND", "OR", "NOT" };

    if (query.charAt(0) == '"' && query.charAt(query.length() - 1) == '"')
      isPhraseMatching = true;

    int i = 1;
    for (String operator : operatorString) {
      int index = query.indexOf(operator);
      if (index != -1) {
        if (operators[0] == 0)
          operators[0] = i;
        else
          operators[1] = i;
      }
      i++;
    }
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

  public String getSnippet(String doc, HashSet<String> tokens) {
    String textContent = Jsoup.parse(doc).text();

    for (String token : tokens) {
      if (textContent.contains(token)) {
        int index = textContent.indexOf(token);
        return textContent.substring(index - windowCharSize,
            index + windowCharSize);
      }
    }

    return "No Snippet Found";
  }

  public String getSnippet(String doc, String phrase) {
    String textContent = Jsoup.parse(doc).text();

    if (textContent.contains(phrase)) {
      int index = textContent.indexOf(phrase);
      return textContent.substring(index - windowCharSize,
          index + windowCharSize);
    }

    return "No Snippet Found";
  }

  private List<ObjectId> rankDocs(String[] tokens) {
    return null;
  }
}
