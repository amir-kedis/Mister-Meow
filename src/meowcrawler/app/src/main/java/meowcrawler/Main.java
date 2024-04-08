package meowcrawler;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
  public static void main(String[] args) {
    URLsHandler urlEx = new URLsHandler();
    String baseURL = "https://en.wikipedia.org";

    RobotsManager rm = new RobotsManager();

    try {
      Document doc = Jsoup.connect(baseURL).get();
      List<String> urls = urlEx.HandleURLs(doc, baseURL);
      for (String url : urls) {
        System.out.println(url);
      }
    } catch (Exception e) {
      System.out.println("An error occured");
    }
  }
}
