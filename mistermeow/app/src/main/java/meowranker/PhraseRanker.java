package meowranker;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;


public class PhraseRanker extends Ranker {

    public PhraseRanker() {
        super();
    }

    // TODO: change function return type
    public List<ObjectId> rank(String query) {


        double[] popularity = getPopularityArr();

        // Tokenizing query
        List<String> searchTokens = tokenizer.tokenizeString(query, true);
        System.out.println(searchTokens);

        // getting docs common in all tokens & matches the query phrase
        List<ObjectId> matchedDocs = getMatchingDocs(searchTokens, query);

        System.out.println(matchedDocs.size()+ " || " + ProcessedDocs.size());
        // for (Document doc : ProcessedDocs) {
        //     System.out.println(doc.getString("URL"));
        //     System.out.println(doc.)
        // }

        // calculating relevance for each document
        List<Double> relevance = this.calculateRelevance(matchedDocs, searchTokens, popularity);

        // for (Double val: relevance){
        // System.out.println(val);
        // }

        List<Map.Entry<ObjectId, Double>> finalRank = combineRelWithPop(matchedDocs, relevance, popularity);

        finalRank.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));


        System.out.println("======================================");
        System.out.println("=========== Final Result =============");
        System.out.println("======================================");

        List<ObjectId> SortedList = new ArrayList<>();
        for (Map.Entry<ObjectId, Double> e : finalRank) {
            SortedList.add(e.getKey());
            System.out.println("URL: "+ db.getDocument(e.getKey().toString()).getString("URL") + "|| Rank = " + e.getValue());
            // The previous printing is time costly, comment it if you're not testing of debugging
        }

        return SortedList;
    }

    private List<ObjectId> getMatchingDocs(List<String> searchTokens, String query) {

        List<ObjectId> docs = getCommonDocs(searchTokens);

        List<ObjectId> finalDocs = new ArrayList<>();

        this.ProcessedDocs = new ArrayList<>();
        for (ObjectId id : docs) {
            Document currDoc = db.getDocument(id.toString());               // getting the document by id
        
            String content = currDoc.getString("content");              // getting the content of the document
            String text = Jsoup.parse(content).text();                      // separating html from content

            String regex = "(?i)" + query;                       // making case-insensitive search
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            boolean flag = matcher.find();

            if (flag) {
                finalDocs.add(currDoc.getObjectId("_id"));              // adding the documents that matches the query 
                ProcessedDocs.add(db.getDocument(id.toString()));           //  Saving documents to access db only once
            }                                                           

        }

        return finalDocs;
    }

    private List<ObjectId> getCommonDocs(List<String> searchTokens) {

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
      
         return docsId;
    }

}
