package src.main.java.meowcrawler;

import java.util.*;

public class QueueManagerMain {
  public static void main(String[] args) {
    QueueManager q = new QueueManager();
    q.push("https://en.wikipedia.org");
    q.push("https://ar.wikipedia.org");
    q.push("https://fr.wikipedia.org");

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
