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

        //list of tokens after tokenization (without stemming)
        List<String> tokens = new ArrayList<>();
        tokens.add("github"); // [ 661b0e19c22b5f3c3cc84f54, 661b0e25c22b5f3c3cc84f5e, 
                                //   661b0e26c22b5f3c3cc84f60, 661b0e27c22b5f3c3cc84f63, 
                                //   661b0e33c22b5f3c3cc84f7a, 661b0e34c22b5f3c3cc84f83 ]
        
        tokens.add("git");    // [ 661b0e27c22b5f3c3cc84f63, 661b0e2dc22b5f3c3cc84f68,
                                //   661b0e33c22b5f3c3cc84f72, 661b0e34c22b5f3c3cc84f83, 
                                //   661b0e34c22b5f3c3cc84f8b ]
       
        List<ObjectId> matchedDocs = r.rank(tokens);         // expected output: [661b0e27c22b5f3c3cc84f63, 661b0e34c22b5f3c3cc84f83]
        
        for(ObjectId id: matchedDocs)
            System.out.println(id);
    }    
}