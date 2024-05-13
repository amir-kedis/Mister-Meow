package meowcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import meowdbmanager.DBManager;
import org.bson.Document;
import org.jsoup.Jsoup;

public class Crawler implements Runnable {
  static private HashingManager hM = new HashingManager();
  static private QueueManager qM = new QueueManager();
  static private DBManager db = new DBManager();
  static private int countOfDocumentsCrawled = 0;
  static private int rankerIndex = 0;

  /**
   * handleHashingURL - takes a Url, hash and check it.
   *
   * @param nUrl      - the Url to handle.
   * @param parent_id - parent url of the current url.
   * @return boolean - true if url is new (not crawled before), else return
   *         false.
   */
  private boolean handleHashingURL(Url nUrl, int parent_id) {
    // Hash and check if the url was not crawled (store it if it wasn't)
    synchronized (hM) {
      String hashedUrl = hM.HashAndCheckURL(nUrl);
      if (hashedUrl == null) {
        synchronized (db) {
          if (parent_id != -1)
            db.updateParents("hashedURL", nUrl.getHashedURL(), parent_id);
        }
        return false;
      }
    }
    return true;
  }

  /**
   * handleHashingDoc - takes a nUrl and handles its document fetching, hashing
   * and insertion into DB.
   *
   * @param nUrl      - the given url.
   * @param doc       - the html document of the url.
   * @param parent_id - parent url of the current url.
   * @return void.
   */
  private void handleHashingDoc(Url nUrl, org.jsoup.nodes.Document doc,
      int parent_id) {
    // Make sure that we are crawling english websites only.
    String docLang = doc.select("html").attr("lang");
    boolean insertOrNot = false;

    if (docLang != null && !docLang.contains("en") && !docLang.contains("ar")) {
      return;
    }

    // Get the text from the html document.
    String outerDoc = doc.outerHtml();

    synchronized (hM) {
      // Hash and check the html document, and push the Url into the queue.
      String hashedDoc = hM.HashAndCheckDoc(nUrl, outerDoc);

      if (hashedDoc != null) {
        insertOrNot = true;
        synchronized (qM) {
          qM.push(nUrl);
          qM.moveToDomainQ();
        }
      }
    }

    this.handleInsertionIntoDB(insertOrNot, nUrl, outerDoc, doc.title(),
        parent_id);
    outerDoc = null;
  }

  /**
   * handleInsertionIntoDB - determines if the Url should be inserted into DB or
   * increment its popularity.
   *
   * @param insertOrNot - boolean to indicate if we should insert or increment
   *                    popularity.
   * @param nUrl        - the nUrl from which we would get its related data.
   * @param doc         - outerHtml document of the url.
   * @param title       - of the document.
   * @param parent_id   - parent url of the current url.
   * @return void.
   */
  private void handleInsertionIntoDB(boolean insertOrNot, Url nUrl, String doc,
      String title, int parent_id) {
    final String ANSI_CYAN = "\u001B[36m";

    // check if the url & its doc needs to be put into the database.
    if (insertOrNot) {

      List<Integer> parents = new ArrayList<>();
      parents.add(parent_id);

      synchronized (db) {
        db.insertDocument(nUrl.getUrlString(), title, nUrl.getDomainName(), doc,
            nUrl.getHashedURL(), nUrl.getHashedDoc(), rankerIndex,
            parents);

        nUrl.setRankerId(rankerIndex);

        System.out.println(ANSI_CYAN + "|| Inserted " + nUrl.getUrlString() +
            " into the database"
            + " Count: " + ++countOfDocumentsCrawled + " ||"
            + " RankerId: " + rankerIndex++ + " ||");
      }
    } else {
      synchronized (db) {
        db.updateParents("hashedDoc", nUrl.getHashedDoc(), parent_id);
      }
    }

    doc = null;
  }

  /**
   * HandleHashing - takes a set of urls and hash and check that they were not
   * crawled before.
   *
   * @param urls - the set of urls extracted from the html document.
   * @return void.
   */
  public void HandleHashing(Set<String> urls, int parent_id) {
    for (String url : urls) {
      // Create a Url object for the url string.
      Url nUrl = new Url(url, 1);
      org.jsoup.nodes.Document doc = null;

      if (!this.handleHashingURL(nUrl, parent_id)) {
        continue;
      }

      // Fetch the document of the url, then hash and check it.
      doc = nUrl.fetchDocument();

      if (doc != null) {
        this.handleHashingDoc(nUrl, doc, parent_id);
      }

      doc = null;
    }
  }

  /**
   * run - the main function of the Crawler in which the whole operations are
   * happening async in each crawler.
   *
   * @return void.
   */
  public void run() {
    while (countOfDocumentsCrawled < 2000) {
      Url url = null;
      org.jsoup.nodes.Document doc = null;

      // Get the top Url from the queue.
      synchronized (qM) {
        try {
          url = qM.pop();
          System.out.println("|| Popped " + url.getUrlString() + " ||");
        } catch (Exception e) {
          // System.out.println("Queue is empty");
          continue;
        }
      }

      synchronized (db) {
        try {
          doc = Jsoup.parse(db.getUrlDocument(url.getUrlString()));
        } catch (Exception e) {
          System.out.println(e.getMessage());
          continue;
        }
      }

      // Extract Urls and handle them, hash and check that they was not crawled
      // before
      URLsHandler urlH = new URLsHandler();
      Set<String> extractedUrls = urlH.HandleURLs(doc, url.getUrlString());

      HandleHashing(extractedUrls, url.getRankerId());

      synchronized (db) {
        if (!db.updateInQueue(url.getUrlString(), false)) {
          System.out.println("Couldn't update the inQueue field of the url");
        }
      }

      doc = null;
    }
  }

  /**
   * loadHashedData - loads the urls data that was stored in the crawler hashing
   * manager the last time it crawled, to prevent crawling same pages again.
   *
   * @return void.
   */
  static public void loadHashedData() {
    List<Document> urlsData = null;

    synchronized (db) {
      urlsData = db.retrieveHashedDataOfUrls();
    }

    synchronized (hM) {
      hM.fillHashedURLs(urlsData);
      hM.fillHashedDocs(urlsData);
    }

    rankerIndex = urlsData.size();
  }

  /**
   * loadQueueData - loads the queue data that was stored in the crawler
   * the last time it crawled, to prevent crawling same pages again.
   *
   * @return void.
   */
  static public void loadQueueData() {
    List<Document> data = null;
    int count = 0;

    synchronized (db) {
      data = db.retrieveUrlsInQueue();
    }

    System.out.println("size of retrieved urls that was in queue: " +
        data.size());

    for (Document urlData : data) {
      try {
        Url url = new Url(urlData.getString("URL"), 1);

        url.setHashedDoc(urlData.getString("hashedDoc"));
        url.setHashedURL(urlData.getString("hashedURL"));
        url.setRankerId(urlData.getInteger("ranker_id"));

        synchronized (qM) {
          qM.push(url);
          qM.moveToDomainQ();
          count++;
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    System.out.println("count of urls added to queue: " + count);
  }

  /**
   * A static function that provides initial seed for the queueManager.
   */
  static public void ProvideSeed(List<Url> urls) {
    Set<String> seeds = urls.stream().map(Url::getUrlString).collect(Collectors.toSet());
    Crawler c = new Crawler();
    c.HandleHashing(seeds, -1);
  }
}
