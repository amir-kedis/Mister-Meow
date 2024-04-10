package src.main.java.meowcrawler;

import java.util.*;

public class QueueManagerMain {
  public static void main(String[] args) {
    QueueManager q = new QueueManager();

    // NOTE: amir-kedis: changed those to url because it wasn't compiling
    // note sure about the priority values though
    q.push(new Url("https://en.wikipedia.org", 1));
    q.push(new Url("https://ar.wikipedia.org", 1));
    q.push(new Url("https://fr.wikipedia.org", 1));

    q.printPriorityQ();

    if (q.moveToDomainQ())
      System.out.println("Sucessfully moved to domain");
    if (q.moveToDomainQ())
      System.out.println("Sucessfully moved to domain");

    q.printDomainQ();

    while (!q.isEmpty()) {
      try {
        Url returnedUrl = q.pop();
        System.out.println("Returned Url: " + returnedUrl.getUrlString());
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }
}
