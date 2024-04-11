package src.main.java.meowindexer;

import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import org.jsoup.Jsoup;
import src.main.java.meowdbmanager.DBManager;
import src.main.java.meowindexer.Tokenizer.Token;

public class Main {
  // Colors for the console output
  static final String ANSI_RESET = "\u001B[0m";
  static final String ANSI_TEAL = "\u001B[36m";
  static final String ANSI_YELLOW = "\u001B[33m";
  static final String ANSI_RED = "\u001B[31m";
  static final String ANSI_GREEN = "\u001B[32m";
  static final String ANSI_PURPLE = "\u001B[35m";

  static private DBManager db = new DBManager();

  public static void main(String[] args) {
    printHello();

    List<Document> unindexedDocs = db.getDocumentsNotIndexed(50);
    Tokenizer tokenizer = new Tokenizer();
    int patchNumber = 0;

    while (unindexedDocs.size() > 0) {
      printPatchNumber(patchNumber);

      for (Document doc : unindexedDocs) {
        String title = doc.getString("title");
        String content = doc.getString("content");
        org.jsoup.nodes.Document contentDoc = Jsoup.parse(content);

        System.out.println(ANSI_PURPLE + "==>Indexing: " + title + ANSI_RESET);
        HashMap<String, Token> tokens = tokenizer.tokenize(contentDoc);
        System.out.println(ANSI_GREEN + "==>Tokens: " + tokens.size() +
            ANSI_RESET);
        try {
          db.insertInverted(doc.getObjectId("_id").toString(), tokens);
        } catch (Exception e) {
          System.out.println(ANSI_RED + "==>Error: " + e.getMessage() +
              ANSI_RESET);
        }

        System.out.println(ANSI_YELLOW + "==>Inserted into db: " + title +
            ANSI_RESET);
      }

      unindexedDocs = db.getDocumentsNotIndexed(50);
      patchNumber++;
    }

    printFinished();
  }

  private static void printHello() {
    System.out.println(ANSI_TEAL);
    System.out.println("====================================");
    System.out.println("||     Hello from Meowindexer!    ||");
    System.out.println("====================================");
    System.out.println("||  Meowindexer is a  cool  tool  ||");
    System.out.println("||  to index webpages for the fa- ||");
    System.out.println("||       ncy MeowMister SE.       ||");
    System.out.println("====================================");
    System.out.println(ANSI_RESET);
  }

  public static void printPatchNumber(int patchNumber) {
    System.out.println(ANSI_GREEN +
        "==============================" + ANSI_RESET);
    System.out.println(ANSI_GREEN + "|| Starting patch number: " + patchNumber +
        " ||" + ANSI_RESET);
    System.out.println(ANSI_GREEN +
        "==============================" + ANSI_RESET);
  }

  public static void printFinished() {
    System.out.println(ANSI_RED +
        "====================================" + ANSI_RESET);
    System.out.println(ANSI_RED + "|| Finished indexing all docs!  ||" +
        ANSI_RESET);
    System.out.println(ANSI_RED +
        "====================================" + ANSI_RESET);
  }
}
