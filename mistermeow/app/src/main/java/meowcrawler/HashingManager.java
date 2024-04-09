package src.main.java.meowcrawler;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;

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
   * @return boolean - true if the url doesn't exist in HashedURLs, false if it
   *         exists.
   */
  public boolean HashAndCheckURL(String url) {
    String hashedURL = this.HashFunction(url);

    if (!this.HashedURLs.contains(hashedURL)) {
      HashedURLs.add(hashedURL);
      return true;
    }
    return false;
  }

  /**
   * Takes a html doc as a string and hash it then check if it is in the
   * HashedDocs or not.
   *
   * @param url - the url in which we would store as a value if the document
   *            isn't
   *            hashed.
   * @param doc - the html doc represented in a string to hash and check on.
   * @return boolean - true if the doc doesn't exist in HashedDocs, false if it
   *         exists.
   */
  public boolean HashAndCheckDoc(String url, String doc) {
    String hashedDoc = this.HashFunction(doc);

    if (!this.HashedDocs.containsKey(hashedDoc)) {
      HashedDocs.put(hashedDoc, url);
      return true;
    }
    return false;
  }
}
