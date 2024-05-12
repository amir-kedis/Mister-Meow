package meowcrawler;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Url {
  private String urlString;
  private int priority;
  private static final int minPriority = 3;
  private String hashedURL;
  private String hashedDoc;
  private int rankerId;

  public Url(String s, int p) {
    this.urlString = s;
    this.priority = p;
    this.hashedURL = null;
    this.hashedDoc = null;
  }

  /**
   * Setter for the rankerId data member.
   *
   * @param id - the value to set with.
   * @return void.
   */
  public void setRankerId(int id) {
    this.rankerId = id;
  }

  /**
   * Getter for the rankerId data member.
   *
   * @return id.
   */
  public int getRankerId() {
    return this.rankerId;
  }

  /**
   * Setter for the hashedURL data member.
   *
   * @param value - the value to set with.
   * @return void.
   */
  public void setHashedURL(String value) {
    this.hashedURL = value;
  }

  /**
   * Setter for the hashedDoc data member.
   *
   * @param value - the value to set with.
   * @return void.
   */
  public void setHashedDoc(String value) {
    this.hashedDoc = value;
  }

  /**
   * Getter for the hashedURL data member.
   *
   * @return hashedURL.
   */
  public String getHashedURL() {
    return this.hashedURL;
  }

  /**
   * Getter for the hashedDoc data member.
   *
   * @return hashedDoc.
   */
  public String getHashedDoc() {
    return this.hashedDoc;
  }

  /**
   * Fetches the htmlDoc of the URL with the urlString datamember and returns
   * it.
   *
   * @return the fetched Document.
   */
  public Document fetchDocument() {
    Document htmlDoc = null;
    try {
      // Fetche the document.
      htmlDoc = Jsoup.connect(this.urlString).get();
    } catch (Exception e) {
      System.out.println("Couldn't fetch docuemnt of url: " + getUrlString());
    }
    return htmlDoc;
  }

  // Overriding toString fn in Obj class to be able to print correctly
  public String toString() {
    return "{ " + this.urlString + ", " + this.priority + " }";
  }

  public int getPriority() {
    return priority;
  }

  public boolean decrementPriority() {
    if (priority > minPriority) {
      priority--;
      return true;
    }
    return false;
  }

  public String getUrlString() {
    return urlString;
  }

  public String getDomainName() {
    return this.urlString.split("/")[2];
    // another sol:
    // return this.urlStirng.replaceAll("http(s)?://|www\\.|/.*", "");
  }
}
