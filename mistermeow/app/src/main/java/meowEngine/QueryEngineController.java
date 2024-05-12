package meowEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.*;

import meowdbmanager.DBManager;

@RestController
@RequestMapping("/")
public class QueryEngineController {
  private DBManager dbManager;
  private List<String> docs;
  private String currentQuery;
  private boolean isPhraseMatching, isFirstTime;
  private String[] phrases;
  private int[] operators; // 0: None, 1: AND, 2: OR, 3: NOT
  private final int numOfDocsInPage = 20, windowCharSize = 100;

  public QueryEngineController() {
    dbManager = new DBManager();
    docs = new ArrayList<>();
    currentQuery = "";
    isPhraseMatching = false;
    isFirstTime = true;
    phrases = new String[3];
    operators = new int[2];
  }

  @GetMapping("/")
  public String sayHello() {
    return "Hello World!";
  }

  @GetMapping("/suggestions")
  public Document getSuggestions(@RequestParam("query") String query) {
    return new Document("data", dbManager.getSuggestions(query, 10));
  }

  @GetMapping("/search")
  public Document searchQuery(
      @RequestParam("query") String query,
      @RequestParam("page") int page) {

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
    List<String> subList = docs.subList(startIndex, endIndex);
    return new Document("data", subList);
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

  private List<String> rankDocs(String[] tokens) {
    return dbManager.getDocs(tokens);
  }
}
