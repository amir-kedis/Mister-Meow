package meowranker;

import java.util.*;

import org.bson.Document;
import org.bson.types.ObjectId;


public class PhraseRanker extends Ranker{

    public PhraseRanker(){
        super();
    }
    
    //type to be changed later
    List<ObjectId> rank(List<String> tokens){
        List<ObjectId> matchedDocs = getMatchingDocs(tokens);
        return matchedDocs;
    }

    private List<ObjectId> getMatchingDocs(List<String> searchTokens){
        List<Document> docs = db.getInvertedIndex(searchTokens.get(0)).getList("docs", Document.class);
        List<ObjectId> docsId = new ArrayList<>();

        for(Document doc :docs){
            docsId.add(doc.getObjectId("_id"));
        }

        for(int i = 1; i<searchTokens.size() ; i++){
            List<Document> currDocs = db.getInvertedIndex(searchTokens.get(i)).getList("docs", Document.class);
            List<ObjectId> currDocsId = new ArrayList<>();

            for(Document doc :currDocs){
                currDocsId.add(doc.getObjectId("_id"));
            }

            docsId.retainAll(currDocsId);
        }

        return docsId;
    }
}