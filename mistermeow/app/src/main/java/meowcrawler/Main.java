package src.main.java.meowcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
  public static void main(String[] args) {

    Url url1 = new Url("https://www.wikipedia.org/", 1);
    Url url2 = new Url("https://www.google.com/", 1);

    url1.FillDocument();
    url2.FillDocument();

    List<Url> urls = new ArrayList<>();
    urls.add(url1);
    urls.add(url2);

    Crawler.ProvideSeed(urls);

    Thread crawler_one = new Thread(new Crawler());
    Thread crawler_two = new Thread(new Crawler());

    crawler_one.start();
    crawler_two.start();

    try {

      crawler_one.join();
      crawler_two.join();
    } catch (Exception e) {
      System.out.println(e);
    }
    /*
     * URLsHandler urlEx = new URLsHandler();
     * String baseURL = "https://en.wikipedia.org";
     *
     * HashingManager hm = new HashingManager();
     * System.out.println(hm.HashFunction("Hello"));
     *
     * RobotsManager rm = new RobotsManager();
     *
     * try {
     * Document doc = Jsoup.connect(baseURL).get();
     * Set<String> urls = urlEx.HandleURLs(doc, baseURL);
     * for (String url : urls) {
     * System.out.println(url);
     * }
     * } catch (Exception e) {
     * System.out.println("An error occured");
     * }
     */
  }
}
