//
// package meowEngine;
//
// import java.util.ArrayList;
// import java.util.List;
// import javax.ws.rs.PathParam;
// import meowdbmanager.DBManager;
// import org.bson.Document;
// import org.bson.types.ObjectId;
// import org.jsoup.Jsoup;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @RequestMapping("/api/search")
// public class QueryEngineController {
//
// private final DBManager dbManager;
// private final int numOfDocsInPage = 20;
// private final int windowCharSize = 100;
//
// public QueryEngineController(DBManager dbManager) {
// this.dbManager = dbManager;
// }
//
// @GetMapping("/suggestions")
// public List<String> getSuggestions(@RequestParam("query") String query) {
// return dbManager.getSuggestions(query, 10);
// }
//
// @PostMapping("/documents/{page}")
// public SearchResponse searchQuery(@PathParam("q") String query,
// @PathParam("page") int page) {
//
// List<Document> results = processQuery(query, page);
// return new SearchResponse(results);
// }
//
// private List<Document> processQuery(String query, int page) {
// // Implement parsing logic (similar to original parse method)
// List<String> tokens = parseQuery(query);
// List<ObjectId> docIds = dbManager.getDocs(tokens.toArray(new String[0]));
//
// // Pagination logic
// int startIndex = page * numOfDocsInPage;
// int endIndex = Math.min(startIndex + numOfDocsInPage, docIds.size());
// List<Document> documents =
// dbManager.getDocuments(docIds.subList(startIndex, endIndex));
//
// // Add snippets and other info (implement getSnippet methods)
// for (Document doc : documents) {
// String snippet = getSnippet(doc.getString("content"), tokens);
// doc.append("snippet", snippet);
// // Add other relevant information to the document as needed
// }
// return documents;
// }
//
// private String getSnippet(String content, List<String> tokens) {
// // Implement snippet generation logic (similar to original getSnippet)
// String textContent = Jsoup.parse(content).text();
// for (String token : tokens) {
// if (textContent.contains(token)) {
// int index = textContent.indexOf(token);
// return textContent.substring(index - windowCharSize,
// index + windowCharSize);
// }
// }
// return "No Snippet Found";
// }
//
// // Implement other methods like getSnippet(String doc, String phrase) if
// // needed
//
// private List<String> parseQuery(String query) {
// // Implement parsing logic to extract tokens from the query
// // You can use similar logic to the original parse method
// List<String> tokens = new ArrayList<>();
// // ... (parsing logic)
// return tokens;
// }
//
// // This class is for structuring the search response (optional)
// public static class SearchResponse {
//
// private final List<Document> results;
//
// public SearchResponse(List<Document> results) { this.results = results; }
//
// public List<Document> getResults() { return results; }
// }
// }
//
// // Replace with your actual implementation of DBManager
// class DBManager {
//
// public List<String> getSuggestions(String query, int limit) {
// // Implement logic to retrieve suggestions from your database
// return new ArrayList<>(); // Replace with actual implementation
// }
//
// public List<Document> getDocuments(String[] tokens) {
// // Implement logic to retrieve documents based on tokens from your database
// return new ArrayList<>(); // Replace with actual implementation
// }
//
// public List<Document> getDocuments(List<ObjectId> docIds) {
// // Implement logic to retrieve documents based on ObjectIds from your
// // database
// return new ArrayList<>(); // Replace with actual implementation
// }
//
// public void insertSuggestion(String query) {
// // Implement logic to insert suggestion into your database
// }
// }
