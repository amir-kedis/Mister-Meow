package src.main.java.meowcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Crawler implements Runnable {
  static private HashingManager hM;
  static private QueueManager qM;
  private String storageFile;

  public Crawler(String fileName) {
    hM = new HashingManager();
    qM = new QueueManager();
    this.storageFile = fileName;
  }

  public List<String> HandleHashing() { return new ArrayList<String>(); }

  public void run() {
    Url url = null;

    synchronized (qM) {
      try {
        url = qM.pop();
      } catch (Exception e) {
        System.out.println("Queue is empty");
      }
    }

    URLsHandler urlH = new URLsHandler();
    /* Set<String> extractedUrls = urlH.HandleURLs(url.Document, url.) */
  }
}
