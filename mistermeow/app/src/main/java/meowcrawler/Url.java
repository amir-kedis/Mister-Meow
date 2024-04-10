package src.main.java.meowcrawler;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Url {
  private String urlString;
  private Document htmlDoc;
  private int priority;
  private static final int minPriority = 3;

  public Url(String s, int p) {
    this.urlString = s;
    this.priority = p;
    htmlDoc = null;
  }

  /**
   * Setter for the htmlDoc data member.
   *
   * @param doc - the html document to set with.
   * @return void.
   */
  public void SetDocument(Document doc) { this.htmlDoc = doc; }

  /**
   * Getter for the htmlDoc data member.
   *
   * @return htmlDoc.
   */
  public Document GetDocument() { return this.htmlDoc; }

  /**
   * Fetches the htmlDoc of the URL with the urlString datamember.
   *
   * @return boolean indicating if the fetching process succedded or not.
   */
  public boolean FillDocument() {
    try {
      // Fetche the document.
      this.htmlDoc = Jsoup.connect(this.urlString).get();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // Overriding toString fn in Obj class to be able to print correctly
  public String toString() {
    return "{ " + this.urlString + ", " + this.priority + " }";
  }

  public int getPriority() { return priority; }

  public boolean decrementPriority() {
    if (priority > minPriority) {
      priority--;
      return true;
    }
    return false;
  }

  public String getUrlString() { return urlString; }

  public String getDomainName() {
    return this.urlString.split("/")[2];
    // another sol:
    // return this.urlStirng.replaceAll("http(s)?://|www\\.|/.*", "");
  }

  /**
   * Getter for the title of the html document.
   *
   * @return the title of the html document.
   */
  public String getTitle() { return this.htmlDoc.title(); }
}
