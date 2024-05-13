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

    protected List<ObjectId> getMatchingDocs(List<String> searchTokens, String query) {

        List<ObjectId> docs = getCommonDocs(searchTokens);

        List<ObjectId> finalDocs = new ArrayList<>();

        ProcessedDocs = new ArrayList<>();

        for (ObjectId id : docs) {
            Document currDoc = db.getDocument(id.toString()); // getting the document by id

            String content = currDoc.getString("content"); // getting the content of the document
            String text = Jsoup.parse(content).text(); // separating html from content

            String regex = "(?i)" + query; // making case-insensitive search
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
