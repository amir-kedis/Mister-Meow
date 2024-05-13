package meowranker;

import java.util.*;
import org.bson.types.ObjectId;

public class Main {
    public static void main(String[] argv) {

        Ranker ranker = new PhraseRanker();

        String query = "The dfl;akjf;asd Free Encyclopedia"; // tests searching for unfound token
        ranker.rank(query);

        query = "The Free Encyclopedia";
        ranker.rank(query);

        // query = "Wikipedia";
        // ranker.rank(query);

        query = "I love you";
        ranker.rank(query);


        ranker = new QueryRanker();
        ranker.rank("The Free Encyclopedia");

        // QueryRanker Qr = new QueryRanker();
        // Qr.rank("Wkiipedia the free encyclopedia");
    }
}
