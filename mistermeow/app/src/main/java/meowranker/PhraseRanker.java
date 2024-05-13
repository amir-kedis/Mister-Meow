package meowranker;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;

public class PhraseRanker extends Ranker {

  public PhraseRanker() { super(); }

  // TODO: change function return type
  public List<Document> rank(String query) {

    // applying PR algorithm
    double[][] M = this.constructUrlsGraph();
    double[] popularity = this.getPopularity(M, M.length);

    // Tokenizing query
    List<String> searchTokens = tokenizer.tokenizeString(query);
    System.out.println(searchTokens);

    // getting docs common in all tokens & matches the query phrase
    List<Document> matchedDocs = getMatchingDocs(searchTokens, query);

    // System.out.println(matchedDocs.size());
    // for (Document doc : matchedDocs) {
    // System.out.println(doc.getString("URL"));
    // }

    // calculating relevance for each document
    List<Double> relevance =
        this.calculateRelevance(matchedDocs, searchTokens, popularity);

    // for (Double val: relevance){
    // System.out.println(val);
    // }

    List<Map.Entry<Document, Double>> finalRank =
        this.combineRelWithPop(matchedDocs, relevance, popularity);

    finalRank.sort(Map.Entry.comparingByValue());

    System.out.println("======================================");
    System.out.println("=========== Final Result =============");
    System.out.println("======================================");

    List<Document> SortedList = new ArrayList<>();
    for (Map.Entry<Document, Double> e : finalRank) {
      SortedList.add(e.getKey());
      System.out.println(e.getKey().getString("URL") + " " + e.getValue());
    }

    // TODO: call function sort by higher rank
    return SortedList;
  }

  private List<Document> getMatchingDocs(List<String> searchTokens,
                                         String query) {

    List<Document> docs = getCommonDocs(searchTokens);

    List<Document> finalDocs = new ArrayList<>();

    for (Document doc : docs) {
      ObjectId id = doc.getObjectId("_id");
      Document currDoc =
          db.getDocument(id.toString()); // getting the document by id

      String content =
          currDoc.getString("content"); // getting the content of the document
      String text = Jsoup.parse(content).text(); // separating html from content

      String regex = "(?i)" + query; // making case-insensitive search
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(text);

      boolean flag = matcher.find();

      if (flag) // checking if the query is a part of the document
        finalDocs.add(currDoc); // adding the documents that matches the query
    }

    return finalDocs;
  }

  private List<Document> getCommonDocs(List<String> searchTokens) {

    if (searchTokens.size() == 0)
      return new ArrayList<>();

    // getting the first token present in db
    int ind = 0;
    Document invertedInd = db.getInvertedIndex(searchTokens.get(0));
    ind++;

    while (invertedInd == null && ind < searchTokens.size()) {
      invertedInd = db.getInvertedIndex(searchTokens.get(ind));
      ind++;
    }

    if (invertedInd == null)
      return new ArrayList<>();

    List<Document> docs = invertedInd.getList("docs", Document.class);
    List<ObjectId> docsId = new ArrayList<>();

    for (Document doc : docs) {
      docsId.add(doc.getObjectId("_id"));
    }

    for (int i = ind; i < searchTokens.size(); i++) {

      invertedInd = db.getInvertedIndex(searchTokens.get(i));
      if (invertedInd != null) {

        List<Document> currDocs = invertedInd.getList("docs", Document.class);
        List<ObjectId> currDocsId = new ArrayList<>();

        for (Document doc : currDocs) {
          currDocsId.add(doc.getObjectId("_id"));
        }

        docsId.retainAll(currDocsId);
      }
    }

    List<Document> commonDocs = new ArrayList<>();
    for (ObjectId id : docsId) {
      commonDocs.add(db.getDocument(id.toString()));
    }

    return commonDocs;
  }
}
