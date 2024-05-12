package meowranker;

import java.util.*;

import org.bson.types.ObjectId;

public class Main {
    public static void main(String[] argv){
        
        // int UrlCount = 4;
        // double[][] M = new double[UrlCount][];

        // M[0] = new double[]{0 , 1 , 0, 0};
        // M[1] = new double[]{0 , 0 , 0.5, 1};
        // M[2] = new double[]{1 , 0 , 0, 0};
        // M[3] = new double[]{0 , 0 , 0.5, 0};

        // Ranker ranker = new Ranker();
        // double[] r = ranker.getPopularity(M , UrlCount);

        // for(int i=0 ; i<UrlCount; i++)
        //     System.out.print(r[i]+ " ");

        // testing phrase matching
        PhraseRanker r = new PhraseRanker();

        String query = "The Free Encyclopedia";
        r.rank(query); 
        
        query = "domestic cat";
        r.rank(query); 
        
    }   
}