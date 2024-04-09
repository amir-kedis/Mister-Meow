package src.main.java.meowcrawler;

import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
  public static void main(String[] args) {
    URLsHandler urlEx = new URLsHandler();
    String baseURL = "https://en.wikipedia.org";

    HashingManager hm = new HashingManager();
    System.out.println(hm.HashFunction("Hello"));

    RobotsManager rm = new RobotsManager();

    try {
      Document doc = Jsoup.connect(baseURL).get();
      Set<String> urls = urlEx.HandleURLs(doc, baseURL);
      for (String url : urls) {
        System.out.println(url);
      }
    } catch (Exception e) {
      System.out.println("An error occured");
    }
  }
}
