package meowranker;

import java.util.*;
import org.bson.types.ObjectId;

public class Main {
    public static void main(String[] argv) {
        
        PhraseRanker phRanker = new PhraseRanker();

        String query = "The dfl;akjf;asd Free Encyclopedia";    //  tests searching for unfound token
        phRanker.rank(query); 
        
        query = "The Free Encyclopedia";                        
        phRanker.rank(query); 
        
        query = "domestic cat";
        phRanker.rank(query); 
        
    }   
}