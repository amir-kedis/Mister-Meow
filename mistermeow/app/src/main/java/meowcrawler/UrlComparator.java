package src.main.java.meowcrawler;

import java.util.*;

// The Higher priority -> comes in front in the queue
public class UrlComparator implements Comparator<Url> {
  public int compare(Url u1, Url u2) {
    if (u1.getPriority() < u2.getPriority())
      return 1;
    else if (u1.getPriority() > u2.getPriority())
      return -1;
    else
      return 0;
  }
}
