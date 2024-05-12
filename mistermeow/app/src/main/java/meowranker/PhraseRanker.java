package meowranker;

import java.util.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseRanker extends Ranker {

    public PhraseRanker() {
        super();
    }

    // TODO: change function return type
    public List<Document> rank(String query) {
        // TODO:Call function to construct M matrix required to calculate popularity
        // TODO: Call function getPopularity()

        List<Document> matchedDocs = getMatchingDocs(query);

        System.out.println(matchedDocs.size());

        for (Document doc : matchedDocs) {
            System.out.println(doc.getString("URL"));
        }

        // TODO: call function sort by higher rank
        return matchedDocs;
    }

    private List<Document> getMatchingDocs(String query) {

        List<String> searchTokens = tokenizer.tokenizeString(query);

        // List<Document> docs = db.getInvertedIndex(firstToken).getList("docs",
        // Document.class);
        List<Document> docs = getCommonDocs(searchTokens);
        List<Document> finalDocs = new ArrayList<>();

        for (Document doc : docs) {
            ObjectId id = doc.getObjectId("_id");
            Document currDoc = db.getDocument(id.toString()); // getting the document by id

            String content = currDoc.getString("content"); // getting the content of the document
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

        List<Document> docs = db.getInvertedIndex(searchTokens.get(0)).getList("docs", Document.class);
        List<ObjectId> docsId = new ArrayList<>();

        for (Document doc : docs) {
            docsId.add(doc.getObjectId("_id"));
        }

        for (int i = 1; i < searchTokens.size(); i++) {
            Document invertedInd = db.getInvertedIndex(searchTokens.get(i));
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