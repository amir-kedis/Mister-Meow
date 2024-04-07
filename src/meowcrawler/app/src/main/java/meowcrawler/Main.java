package meowcrawler;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
  public static void main(String[] args) {
    URLsHandler urlEx = new URLsHandler();

    try {
      Document doc =
          Jsoup.connect("https://zagsystems.org/softwaregia24").get();
      List<String> urls =
          urlEx.HandleURLs(doc, "https://zagsystems.org/softwaregia24");
      for (String url : urls) {
        System.out.println(url);
      }
    } catch (Exception e) {
      System.out.println("An error occured");
    }
  }
}
