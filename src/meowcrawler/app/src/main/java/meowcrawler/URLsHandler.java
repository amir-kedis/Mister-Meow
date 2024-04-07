package meowcrawler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLsHandler {

  /**
   * Extract URLs from an html document and Normalize them.
   *
   * @param baseURL: the current html document URL.
   * @param html:    the html document to extract and normalize urls from.
   * @return List of URLs.
   */
  public List<String> HandleURLs(Document html, String baseURL) {
    List<String> urls = new ArrayList<String>();

    /*
     * Extract anchor tags from the html document, then extract urls from each a
     * tag
     */
    Elements anchorTags = html.select("a");
    for (Element link : anchorTags) {
      urls.add(link.attr("href"));
    }

    // Call a private function to normalize the urls.
    urls = NormalizeURLs(urls, baseURL);

    return urls;
  }

  /**
   * Normalize a list of urls, based on a baseURL.
   *
   * @param urls:    the list of urls represented as strings.
   * @param baseURL: the base url in which we normalize upon the other urls.
   * @returns a list of normalized urls of type string.
   */
  private List<String> NormalizeURLs(List<String> urls, String baseURL) {
    List<String> normalizedURLs = new ArrayList<String>();

    // Get the absolute URL from the base url.
    URI newBaseURL = URI.create(baseURL);
    String absoluteURL =
        newBaseURL.getScheme() + "://" + newBaseURL.getAuthority();

    // Loop over urls to normalize them
    for (String url : urls) {
      URI newURL = URI.create(url);
      String normalizedURL = newURL.normalize().toString();

      // If url not absolute, make it absolute relative to the base url.
      if (!newURL.isAbsolute()) {
        if (newURL.toString().startsWith("//")) {
          normalizedURL = newBaseURL.getScheme() + ":" + newURL.toString();
        } else {
          normalizedURL = absoluteURL + newURL.toString();
        }
      }

      // Add the normalized URL to the list
      normalizedURLs.add(normalizedURL);
    }

    return normalizedURLs;
  }
}
