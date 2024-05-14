package meowranker;

import java.util.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;

public class QueryRanker extends Ranker{
    
    public List<Double> QueryDocRel;

    public QueryRanker(){
        super();
        QueryDocRel = new ArrayList<>();
    }

    protected List<ObjectId> getMatchingDocs(List<String> searchTokens, String query){
        List<ObjectId> docsUnion = db.getDocIDs(searchTokens);

        Map<ObjectId , Integer> docRepetition = new HashMap<>();

        List<ObjectId> distinctDocs = new ArrayList<>();

        ProcessedDocs = new ArrayList<>();
        for(ObjectId docId : docsUnion){
            if(docRepetition.containsKey(docId)){
                // System.out.println(docId);
                int currRep = docRepetition.get(docId);
                docRepetition.remove(docId);
                docRepetition.put(docId , currRep+1);
            }
            else{
                distinctDocs.add(docId);
                docRepetition.put(docId ,1 ); 
                ProcessedDocs.add(db.getDocument(docId.toString()));
            }
        }
        
        for(ObjectId docId :distinctDocs){
            QueryDocRel.add((double)docRepetition.get(docId)/(double)searchTokens.size());
        }

        return distinctDocs;
    }    

    protected List<Double> addQueryDocRel(List<Double> relevance){
        int ind = 0;
        for(Double val:relevance){
            if(ind >= QueryDocRel.size())
                break;
            val*=QueryDocRel.get(ind);
            ind++;
        }

        return relevance;   
    }
}
