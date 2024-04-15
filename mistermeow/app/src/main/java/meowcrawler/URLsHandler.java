package meowcrawler;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLsHandler {

  /**
   * Extract URLs from an html document and Normalize them.
   *
   * @param baseURL: the current html document URL.
   * @param html:    the html document to extract and normalize urls from.
   * @return Set of URLs.
   */
  public Set<String> HandleURLs(Document html, String baseURL) {
    Set<String> urls = new HashSet<>();

    /*
     * Extract anchor tags from the html document, then extract urls from each a
     * tag
     */
    Elements anchorTags = html.select("a");
    for (Element link : anchorTags) {
      urls.add(link.attr("href"));
    }

    try {
      // Call a private function to normalize the urls.
      urls = NormalizeURLs(urls, baseURL);
    } catch (Exception e) {
      System.out.println(e);
      return urls;
    }

    RobotsManager robotsM = new RobotsManager();
    urls = robotsM.ExcludeRobotsURLs(urls, baseURL);

    return urls;
  }

  /**
   * Normalize a Set of urls, based on a baseURL.
   *
   * @param urls:    the Set of urls represented as strings.
   * @param baseURL: the base url in which we normalize upon the other urls.
   * @returns a Set of normalized urls of type string.
   */
  private Set<String> NormalizeURLs(Set<String> urls, String baseURL) {
    Set<String> normalizedURLs = new HashSet<>();

    // Get the absolute URL from the base url.
    URI newBaseURL = URI.create(baseURL);

    String scheme = newBaseURL.getScheme();
    if (scheme == null) {
      scheme = "https";
    }

    String absoluteURL = scheme + "://" + newBaseURL.getAuthority();

    // Loop over urls to normalize them
    for (String url : urls) {
      URI newURL = URI.create(url);
      String normalizedURL = newURL.normalize().toString();

      // If url not absolute, make it absolute relative to the base url.
      if (!newURL.isAbsolute()) {
        if (newURL.toString().startsWith("//")) {
          normalizedURL = scheme + ":" + newURL.toString();
        } else if (newURL.toString().startsWith("/")) {
          normalizedURL = absoluteURL + newURL.toString();
        } else {
          normalizedURL = scheme + "://" + newURL.toString();
        }
      }

      if (!normalizedURL.endsWith("/")) {
        normalizedURL += "/";
      }
      // Add the normalized URL to the Set
      normalizedURLs.add(normalizedURL);
    }

    return normalizedURLs;
  }
}
