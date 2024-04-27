package meowcrawler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;

public class HashingManager {
  private Set<String> HashedURLs;
  private Map<String, String> HashedDocs;

  /**
   * Constructor - Initialize the HashedURLs and the HashedDocs data members.
   */
  public HashingManager() {
    HashedURLs = new HashSet<>();
    HashedDocs = new HashMap<>();
  }

  /**
   * Takes a string and hash it using md5Hex.
   *
   * @params str - the string to hash.
   * @return the hashed string.
   */
  public String HashFunction(String str) {
    String hashedVal = DigestUtils.md5Hex(str);
    return hashedVal;
  }

  /**
   * Takes a url and hash it then check if it is in the HashedURLs or not.
   *
   * @param url - the url to hash and check on.
   * @return String - returns the hashed url if it wasn't hashed before, else
   *         returns null.
   */
  public String HashAndCheckURL(Url url) {
    String hashedURL = this.HashFunction(url.getUrlString());
    url.setHashedURL(hashedURL);

    if (!this.HashedURLs.contains(hashedURL)) {
      HashedURLs.add(hashedURL);
      return hashedURL;
    }
    return null;
  }

  /**
   * Takes a html doc as a string and hash it then check if it is in the
   * HashedDocs or not.
   *
   * @param url - the url in which we would store as a value if the document
   *            isn't
   *            hashed.
   * @param doc - the html doc represented in a string to hash and check on.
   * @return String - returns the hashed document if it wasn't hashed before,
   *         else
   *         returns null.
   */
  public String HashAndCheckDoc(Url url, String doc) {
    String hashedDoc = this.HashFunction(doc);
    url.setHashedDoc(hashedDoc);

    if (!this.HashedDocs.containsKey(hashedDoc)) {
      HashedDocs.put(hashedDoc, url.getUrlString());
      return hashedDoc;
    }
    return null;
  }

  /**
   * fillHashedURLs - takes a list of hashed URLS and fill the hashedURLs set
   * with it.
   *
   * @param List<Document> - the list of Documents which contains the
   *                       hashedURLs.
   * @return void.
   */
  public void fillHashedURLs(List<Document> urlsData) {
    for (Document urlData : urlsData) {
      String hashedURL = urlData.getString("hashedURL");

      if (!this.HashedURLs.contains(hashedURL)) {
        this.HashedURLs.add(hashedURL);
      }
    }
    System.out.println("count of urls added to hashed urls: " +
                       HashedURLs.size());
  }

  /**
   * fillHashedDocs - takes a list of urls data and fill the hashedDocs map
   * with it.
   *
   * @param List<Document> - the list of Documents which contains the
   *                       hashedDocs.
   * @return void.
   */
  public void fillHashedDocs(List<Document> urlsData) {
    for (Document urlData : urlsData) {
      String hashedDoc = urlData.getString("hashedDoc");
      String url = urlData.getString("URL");

      if (!this.HashedDocs.containsKey(hashedDoc)) {
        HashedDocs.put(hashedDoc, url);
      }
    }
    System.out.println("count of urls added to hashed docs: " +
                       HashedDocs.size());
  }
}
