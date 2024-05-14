package meowranker;

public class Main {
    public static void main(String[] argv) {

        // Ranker ranker = new PhraseRanker();
        //
        // String query = "The dfl;akjf;asd Free Encyclopedia"; // tests searching for
        // unfound token
        // ranker.rank(query);
        //
        // query = "The Free Encyclopedia";
        // ranker.rank(query);
        //
        // // query = "Wikipedia";
        // // ranker.rank(query);
        //
        // query = "I love you";
        // ranker.rank(query);

        Ranker ranker = new QueryRanker();
        ranker.rank("cats");

        // QueryRanker Qr = new QueryRanker();
        // Qr.rank("Wkiipedia the free encyclopedia");
    }
}
