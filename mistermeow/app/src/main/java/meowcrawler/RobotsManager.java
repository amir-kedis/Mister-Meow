package meowcrawler;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RobotsManager {

  static private Map<String, Set<String>> storedRobots = new HashMap<>();

  /*
   * Takes a list of urls and a base url, excludes any url ,that is in the
   * robots file, from the list
   *
   * @param urls: the list of urls to exclude from.
   *
   * @param baseUrlStr: the base url from which we would fetch the robots file
   *
   * @return a list of urls that IS NOT in the robots file.
   */
  public Set<String> ExcludeRobotsURLs(Set<String> urls, String baseUrlStr) {
    Set<String> newUrlsList = new HashSet<>();
    Set<String> ExcludedUrls = null;

    // Convert the baseUrlStr into a URI object.
    URI baseUrl = URI.create(baseUrlStr);

    ExcludedUrls = checkAndFetchExcluded(baseUrl);
    if (ExcludedUrls == null) {
      return newUrlsList;
    }

    // For each url in the urls list, if it contains any url from the robots
    // file, exclude it.
    for (String url : urls) {
      boolean isExcluded = false;

      // Check if the url contains any excluded urls or not.
      for (String eUrl : ExcludedUrls) {
        if (url.contains(eUrl)) {
          isExcluded = true;
          continue;
        }
      }

      if (!isExcluded) {
        newUrlsList.add(url);
      }
    }

    return newUrlsList;
  }

  /**
   * Takes a baseUrl and check if, for its host name, the robot file was
   * fetched before or not, and fetch it if it wasn't.
   *
   * @param baseUrl: the baseUrl from which we would get the hostname.
   * @return Set of excluded urls.
   */
  private Set<String> checkAndFetchExcluded(URI baseUrl) {
    Set<String> ExcludedUrls = null;
    boolean isFetched = false;

    synchronized (storedRobots) {
      isFetched = storedRobots.containsKey(baseUrl.getHost());
    }

    // Store each excluded urls with their hostname, to not fetch them again.
    if (!isFetched) {
      // Fetch robots file as a document, then extract disallowed urls from it.
      Document robotsDoc = FetchRobots(baseUrl);
      if (robotsDoc == null) {
        return null;
      }

      ExcludedUrls = ExtractDisallowedURLs(robotsDoc);

      synchronized (storedRobots) {
        storedRobots.put(baseUrl.getHost(), ExcludedUrls);
      }

    } else {
      synchronized (storedRobots) {
        ExcludedUrls = storedRobots.get(baseUrl.getHost());
      }
    }

    return ExcludedUrls;
  }

  /**
   * It takes a base URL as a URI and fetches the robots.txt file for the
   * website
   * if it exists.
   *
   * @param baseUrl: the base url as a URI to fetch from the robots.txt
   * @return the robots file as a Document.
   */
  public Document FetchRobots(URI baseUrl) {

    // Get the robots.txt file of the website, using the hostname.
    String robotsURL =
        baseUrl.getScheme() + "://" + baseUrl.getHost() + "/robots.txt";

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
  public Set<String> ExtractDisallowedURLs(Document robots) {
    Set<String> disallowedUrls = new HashSet<>();

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
