package meowranker;

import java.util.*;
import org.bson.types.ObjectId;

public class Main {
    public static void main(String[] argv) {

        Ranker ranker = new Ranker();

        double[][] M = ranker.constructUrlsGraph();
       
        double[] R = ranker.getPopularity(M , M.length);

        for(int i=0 ; i<M.length; i++)
            System.out.println(R[i]);

        // testing phrase matching
        // PhraseRanker phRanker = new PhraseRanker();

        // String query = "The Free Encyclopedia";
        // phRanker.rank(query); 
        
        // query = "domestic cat";
        // phRanker.rank(query); 
        
    }   
}