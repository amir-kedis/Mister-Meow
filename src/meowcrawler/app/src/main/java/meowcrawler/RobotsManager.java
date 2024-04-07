package meowcrawler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  /**
   * Takes a robots document and a base url and extract disallowed URLs from it.
   *
   * @param robots: the document in which we would extract disallowed urls.
   * @return a list of disallowed URLs.
   */
  public List<String> ExtractDisallowedURLs(Document robots) {
    List<String> disallowedUrls = new ArrayList<String>();

    // Convert the robots file into a text file and store it in a string.
    String robotsText = robots.body().text();

    // The regex in which we would match upon (matches the disallowed urls)
    Pattern pattern = Pattern.compile("Disallow:\s([^\s\n]*)");
    Matcher matcher = pattern.matcher(robotsText);

    // Extract all matched urls from the robots file.
    while (matcher.find()) {
      String match = matcher.group(1);

      if (match.length() > 1) {
        disallowedUrls.add(match);
      }
    }

    // Return the list of disallowed urls.
    return disallowedUrls;
  }
}
