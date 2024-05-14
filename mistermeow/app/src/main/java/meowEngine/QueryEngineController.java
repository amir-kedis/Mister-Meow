package meowEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meowdbmanager.DBManager;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.*;

import meowdbmanager.DBManager;
import meowindexer.Tokenizer;
import meowranker.PhraseRanker;

//TODO: normal queries with ranking
//TODO: bold in snippts
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/")
public class QueryEngineController {
  private DBManager dbManager;
  private Tokenizer tokenizer;
  private PhraseRanker phraseRanker;
  private List<ObjectId> docs;
  private String currentQuery;
  private boolean isPhraseMatching, isFirstTime;
  private String[] phrases;
  private int[] operators; // 0: None, 1: AND, 2: OR, 3: NOT
  private List<String> tokens, tags, suggestions;
  private int resultCount;
  private final int numOfDocsInPage = 20, windowCharSize = 100;

  public QueryEngineController() {
    dbManager = new DBManager();
    tokenizer = new Tokenizer();
    phraseRanker = new PhraseRanker();
    docs = new ArrayList<>();
    currentQuery = "";
    isPhraseMatching = false;
    isFirstTime = true;
    phrases = new String[3];
    operators = new int[2];
    tags = new ArrayList<>();
    tokens = new ArrayList<>();
    suggestions = new ArrayList<>();
    resultCount = 0;
  }

  @GetMapping("/")
  public String sayHello() {
    return "Hello World!";
  }

  @GetMapping("/suggestions")
  public Document getSuggestions(@RequestParam("query") String query) {
    suggestions = dbManager.getSuggestions(query, 10);
    return new Document("data", suggestions);
  }

  @GetMapping("/search")
  public Document searchQuery(@RequestParam("query") String query,
      @RequestParam("page") int page) {

    if (!query.equals(currentQuery))
      isFirstTime = true;

    if (isFirstTime) {
      currentQuery = query;
      parse(currentQuery);
      dbManager.insertSuggestion(currentQuery);
      tokens = tokenizer.tokenizeString(currentQuery, false);
      tags = tokenizer.tokenizeString(currentQuery, false);
      docs = rankDocs();
      isFirstTime = false;
      resultCount = docs.size();
      suggestions = dbManager.getSuggestions(query, 10);
    }

    int startIndex = (page - 1) * numOfDocsInPage;
    int endIndex = Math.min(startIndex + numOfDocsInPage, docs.size());
    List<ObjectId> subList = docs.subList(startIndex, endIndex);
    return new Document("data", getResults(subList));
  }

  private void parse(String query) {
    isPhraseMatching = true;
    operators[0] = operators[1] = 0;
    phrases[0] = phrases[1] = phrases[2] = null;

    Matcher phraseMatch = Pattern.compile("\"[^\"]+\"").matcher(query);
    Matcher operatorMatch = Pattern.compile("\"\\s*(AND|OR|NOT)\\s*\"").matcher(query);

    int i = 0;
    while (phraseMatch.find()) {
      String phrase = phraseMatch.group().replaceAll("^\"|\"$", "").trim();
      phrases[i++] = phrase;
    }

    if (phrases[0] == null) {
      isPhraseMatching = false;
    } else {
      i = 0;
      while (operatorMatch.find()) {
        String operator = operatorMatch.group().replaceAll("^\"|\"$", "").trim();
        operators[i++] = operator.equals("AND") ? 1
            : operator.equals("OR") ? 2
                : 3;
      }
    }

  }

  private Document getResults(List<ObjectId> docs) {

    List<Document> results = dbManager.getDocuments(docs);
    int availableCount = resultCount;

    for (Document result : results) {
      String doc = result.getString("content");
      String snippet = isPhraseMatching ? getSnippet(doc)
          : getSnippet(doc);
      result.remove("content");
      result.remove("_id");
      result.append("snippets", snippet);
      if (snippet == null)
        availableCount--;
    }

    System.out.println("Results: " + results);
    Document data = new Document("results", results)
        .append("count", resultCount)
        .append("availableCount", availableCount)
        .append("tags", tags)
        .append("suggestions", suggestions);

    return data;
  }

  public String getSnippet(String doc) {
    String textContent = Jsoup.parse(doc).text();
    List<String> strings = isPhraseMatching ? Arrays.asList(phrases) : tokens;

    for (String string : strings) {
      Matcher stringMatch = Pattern.compile("\\b" + string + "\\b").matcher(textContent);
      if (stringMatch.find()) {
        int index = stringMatch.start();
        int start = Math.max(0, index - windowCharSize);
        int end = Math.min(textContent.length(), index + windowCharSize);
        return textContent.substring(start, end);
      }
    }

    return null;
  }

  private List<ObjectId> rankDocs() {
    if (isPhraseMatching) {
      List<ObjectId> docIDs = phraseRanker.rank(phrases[0]);
      if (phrases[1] != null)
        useOperator(docIDs, operators[0], 1);
      if (phrases[2] != null)
        useOperator(docIDs, operators[1], 2);
      System.out.println("DocIDs: " + docIDs);
      return docIDs;
    }
    return dbManager.getDocIDs(tags);
  }

  private void useOperator(List<ObjectId> docIDs, int operator, int phraseIndex) {
    System.out.println("Operator: " + operator + " PhraseIndex: " + phraseIndex);
    if (operator == 1)
      docIDs.retainAll(phraseRanker.rank(phrases[phraseIndex]));
    else if (operator == 2)
      docIDs.addAll(phraseRanker.rank(phrases[phraseIndex]));
    else if (operator == 3)
      docIDs.removeAll(phraseRanker.rank(phrases[phraseIndex]));
  }
}
