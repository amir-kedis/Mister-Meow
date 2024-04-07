package meowcrawler;

import java.net.URI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RobotsManager {

  /**
   * It takes a base URL as a string and fetches the robots.txt file for the
   * website
   * if it exists.
   *
   * @param baseUrlStr: the base url as a string to fetch from the robots.txt
   * @return the robots file as a Document.
   */
  public Document FetchRobots(String baseUrlStr) {
    // Convert the baseUrlStr into a URI object.
    URI baseUrl = URI.create(baseUrlStr);

    // Get the robots.txt file of the website, using the hostname.
    String robotsURL =
        baseUrl.getScheme() + "://" + baseUrl.getAuthority() + "/robots.txt";

    Document robotsFile = null;

    try {
      // Try fetching the robots.txt file.
      robotsFile = Jsoup.connect(robotsURL).get();

    } catch (Exception e) {
      // Print the an error indicating that the robots file doesn't exist for
      // this url.
      System.out.println("robots.txt file doesn't exist for this URL: " +
                         baseUrl.getScheme() + "://" + baseUrl.getAuthority());
    }

    return robotsFile;
  }
}
