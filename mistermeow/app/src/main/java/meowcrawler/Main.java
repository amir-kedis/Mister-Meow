package src.main.java.meowcrawler;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    // NOTE: amir-kedis: I refactored this to change the seed manually for
    // testing
    // TODO: chagnge this seed to be writeen from a file and the number of
    // threads to be inputed

    List<Url> urls = new ArrayList<>();
    urls.add(new Url("https://en.wikipedia.org/wiki/Cat", 1));
    urls.add(new Url("https://en.wikipedia.org/wiki/Dog", 1));
    urls.add(new Url("https://en.wikipedia.org/wiki/Bird", 1));
    urls.add(new Url(
        "https://www.nationalgeographic.com/animals/mammals/facts/domestic-cat",
        1));

    for (Url url : urls) {
      url.FillDocument();
    }

    Crawler.ProvideSeed(urls);

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 16; i++) {
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
