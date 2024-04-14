package meowcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import meowdbmanager.DBManager;

public class Crawler implements Runnable {
  static private HashingManager hM = new HashingManager();
  static private QueueManager qM = new QueueManager();
  static private DBManager db = new DBManager();

  /**
   * HandleHashing - takes a set of urls and hash and check that they were not
   * crawled before.
   *
   * @param urls - the set of urls extracted from the html document.
   * @return list of unique urls that were not crawled before.
   */
  static public List<Url> HandleHashing(Set<String> urls) {
    List<Url> finalUrls = new ArrayList<>();
    final String ANSI_CYAN = "\u001B[36m";

    for (String url : urls) {
      // Hash and check if the url was not crawled (store it if it wasn't)
      synchronized (hM) {
        if (!hM.HashAndCheckURL(url)) {
          continue;
        }
      }

      // Create a Url object for the url string.
      Url nUrl = new Url(url, 1);

      // Fetch the document of the url, then hash and check it.
      if (nUrl.FillDocument()) {
        // Get the text from the html document.
        String doc = nUrl.GetDocument().outerHtml();
        boolean insertOrNot = false;

        synchronized (hM) {
          // Hash and check the html document, and push the Url into the queue,
          // if the doc is new.
          if (hM.HashAndCheckDoc(nUrl.getUrlString(), doc)) {
            insertOrNot = true;

            synchronized (qM) {
              qM.push(nUrl);
              qM.moveToDomainQ();
            }

            finalUrls.add(nUrl);
          }
        }

        // check if the url & its doc needs to be put into the database.
        if (insertOrNot) {
          synchronized (db) {
            db.insertDocument(nUrl.getUrlString(), nUrl.getTitle(),
                              nUrl.getDomainName(), doc);
            System.out.println(ANSI_CYAN + "|| Inserted " +
                               nUrl.getUrlString() + " into the database ||");
          }
        }
      }
    }

    return finalUrls;
  }

  /**
   * run - the main function of the Crawler in which the whole operations are
   * happening async in each crawler.
   *
   * @return void.
   */
  public void run() {
    final int depth = 3;
    int i = 0;
    while (i < depth) {
      Url url = null;

      // Get the top Url from the queue.
      synchronized (qM) {
        try {
          url = qM.pop();
          System.out.println("|| Popped " + url.getUrlString() + " ||");
        } catch (Exception e) {
          System.out.println("Queue is empty");
          continue;
        }
      }

      // TODO: handle that the number of crawled urls doesn't exceed 6000.

      // Extract Urls and handle them, hash and check that they was not crawled
      // before
      URLsHandler urlH = new URLsHandler();
      Set<String> extractedUrls =
          urlH.HandleURLs(url.GetDocument(), url.getUrlString());

      List<Url> urls = HandleHashing(extractedUrls);

      i++;
    }
  }

  /**
   * A static function that provides initial seed for the queueManager.
   */
  static public void ProvideSeed(List<Url> urls) {
    // FIXME: amir-kedis: Akram, What I did is probably wrong, Review/Refactor
    // this please.
    Set<String> seeds =
        urls.stream().map(Url::getUrlString).collect(Collectors.toSet());
    HandleHashing(seeds);
  }
}
