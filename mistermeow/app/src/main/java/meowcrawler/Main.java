package meowcrawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
  public static void main(String[] args) {
    // NOTE: amir-kedis: I refactored this to change the seed manually for
    // testing
    // TODO: chagnge this seed to be writeen from a file and the number of
    // threads to be inputed

    /*
     * URLsHandler urlH = new URLsHandler();
     *
     * Url url = new Url("https://about.fandom.com/mediakit/", 1);
     * url.FillDocument();
     * Set<String> s = urlH.HandleURLs(url.GetDocument(), url.getUrlString());
     * for (String u : s) {
     * System.out.println(u);
     * }
     * System.out.println("----------------------------------------------------");
     * List<Url> l = Crawler.HandleHashing(s);
     * for (Url u : l) {
     * System.out.println(u.getUrlString());
     * }
     */

    List<Url> urls = new ArrayList<>();

    // read seed.txt
    try {
      // NOTE: root path is src/meowindexer/app/. everything is relative to this
      // path
      BufferedReader reader =
          new BufferedReader(new FileReader("../data/seed.txt"));
      String line;
      while ((line = reader.readLine()) != null) {
        urls.add(new Url(line, 3));
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Crawler.ProvideSeed(urls);

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      Thread t = new Thread(new Crawler());
      threads.add(t);
      t.start();
    }

    try {
      for (Thread t : threads) {
        t.join();
      }

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
