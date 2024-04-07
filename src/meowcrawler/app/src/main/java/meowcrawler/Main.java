package meowcrawler;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
  public static void main(String[] args) {
    URLsHandler urlEx = new URLsHandler();

    RobotsManager rm = new RobotsManager();
    rm.FetchRobots("https://en.wikipedia.org/wiki/Main_Page");

    try {
      Document doc =
          Jsoup.connect("https://en.wikipedia.org/wiki/Main_Page").get();
      List<String> urls =
          urlEx.HandleURLs(doc, "https://en.wikipedia.org/wiki/Main_Page");
      for (String url : urls) {
        System.out.println(url);
      }
    } catch (Exception e) {
      System.out.println("An error occured");
    }
  }
}
