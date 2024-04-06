package meowindexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import opennlp.tools.stemmer.PorterStemmer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class tokenizer {
  // NOTE: SUBCLASS Token
  // =====================

  /**
   * Token class: represents a token with its count and position in the document
   *
   * @word: The token
   * @count: The number of times the token appears in the document
   * @position: The position of the token in the document (title, h1, h2, or
   *            other)
   *
   */
  public class Token {
    public String word;
    public int count;
    public String position;

    /**
     * Constructor for the Token class
     *
     * @param word: The token
     */
    public Token(String word) {
      this.word = word;
      this.count = 1;
      this.position = "other";
    }

    /**
     * Increment the count of the token
     */
    public void increment() { count++; }
  }

  // NOTE: CLASS tokenizer |||| Data Members
  private HashSet<String> stopWords;

  /**
   * Constructor for the tokenizer: loads
   */
  public tokenizer() {
    stopWords = new HashSet<String>();
    loadStopWords("stopwords-en.txt");
  }

  public HashMap<String, Token> tokenize(Document doc) {
    String text = doc.text();
    List<String> tokens = tokenizeString(text);
    tokens = stemTokens(tokens);
    HashMap<String, Token> tokenMap = new HashMap<String, Token>();

    for (String token : tokens) {
      if (tokenMap.containsKey(token)) {
        tokenMap.get(token).increment();
      } else {
        tokenMap.put(token, new Token(token));
      }
    }

    fillPosistions(tokenMap, doc);

    return tokenMap;
  }

  /**
   * Load stop words from a file (stopwords-en.txt)
   *
   * @param filename: Name of the file containing stop words
   */
  private void loadStopWords(String filename) {
    try {
      // NOTE: root path is src/meowindexer/app/. everything is relative to this
      // path
      BufferedReader reader =
          new BufferedReader(new FileReader("../data/" + filename));
      String line;
      while ((line = reader.readLine()) != null) {
        stopWords.add(line.trim());
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tokenize a string into words and remove non-alphabetic characters
   *
   * @param text: String to tokenize
   * @return List of tokens
   */
  private List<String> tokenizeString(String text) {
    List<String> tokens = new ArrayList<String>();

    String cleanText = text.toLowerCase().replaceAll("[^a-zA-Z ]", "");
    String[] words = cleanText.split("\\s+");

    for (String word : words) {
      if (word.length() > 1 && !isStopWord(word.trim())) {
        tokens.add(word.trim());
      }
    }

    return tokens;
  }

  /**
   * Stem tokens using the Porter Stemmer
   *
   * @param tokens: List of tokens to stem
   * @return List of stemmed tokens
   */
  private List<String> stemTokens(List<String> tokens) {
    List<String> stemmedTokens = new ArrayList<String>();
    PorterStemmer stemmer = new PorterStemmer();

    for (String token : tokens) {
      stemmedTokens.add(stemmer.stem(token));
    }

    return stemmedTokens;
  }

  /**
   * Check if a word is a stop word
   *
   * @param word: Word to check
   * @return True if the word is a stop word, false otherwise
   */
  private boolean isStopWord(String word) { return stopWords.contains(word); }

  /**
   * Fill the position of each token in the document (title, h1, h2, or other)
   *
   * @param tokens: Map of tokens
   * @param doc:    Document to search for token positions
   */
  private void fillPosistions(HashMap<String, Token> tokens, Document doc) {
    List<String> titleTokens = stemTokens(tokenizeString(doc.title()));
    List<String> h1Tokens = stemTokens(tokenizeString(doc.select("h1").text()));
    List<String> h2Tokens = stemTokens(tokenizeString(doc.select("h2").text()));

    for (String token : tokens.keySet()) {
      Token t = tokens.get(token);
      if (titleTokens.contains(t.word)) {
        t.position = "title";
      } else if (h1Tokens.contains(t.word)) {
        t.position = "h1";
      } else if (h2Tokens.contains(t.word)) {
        t.position = "h2";
      }
    }
  }

  /**
   * Test the tokenizer
   * visit https://en.wikipedia.org/wiki/Cat and print the tokens
   */
  public void test() {
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_GREEN = "\u001B[32m";

    System.out.println(ANSI_GREEN);
    System.out.println("Testing tokenizer...");
    System.out.println(ANSI_RESET);

    Document doc = null;
    final String url = "https://en.wikipedia.org/wiki/Cat";
    try {
      doc = Jsoup.connect(url).get();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("tokenizing: " + url + " : " + doc.title());
    HashMap<String, Token> tokens = tokenize(doc);
    // print sorted by count
    System.out.println("Sorted by count:");
    final String ANSI_YELLOW = "\u001B[33m";
    final String ANSI_RESET2 = "\u001B[0m";
    tokens.entrySet()
        .stream()
        .sorted((e1, e2) -> e1.getValue().count - e2.getValue().count)
        .forEach(e
                 -> System.out.println(ANSI_YELLOW + "{ "
                                       + "word: " + e.getKey() + ", "
                                       + "count: " + e.getValue().count + ", "
                                       + "position: " + e.getValue().position +
                                       " }" + ANSI_RESET2));
  }
}
