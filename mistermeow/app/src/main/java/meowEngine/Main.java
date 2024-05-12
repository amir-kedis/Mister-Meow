package meowEngine;

import java.util.HashSet;
import meowdbmanager.DBManager;

public class Main {
  public static void main(String[] args) {
    RestApplication api = new RestApplication();
    queryEngine qe = new queryEngine();
    DBManager dbManager = new DBManager();

    String doc = dbManager.getUrlDocument("https://en.wikipedia.org/wiki/Cat");
    HashSet<String> query = new HashSet<String>();
    query.add("cat");
    query.add("kitten");

    String phrase = "cats can";
    System.out.println(qe.getSnippet(doc, query));
    System.out.println(qe.getSnippet(doc, phrase));
  }
}
