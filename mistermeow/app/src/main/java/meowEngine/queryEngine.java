package meowEngine;

import java.util.List;

import javax.ws.rs.*;
import meowindexer.Tokenizer;

/*
 * get search query from frontend
 * tokenize and do all other necessary processing
 * search in the indexer
 * get all docs that match the query
 * use ranker + prioritize actual token over stemmed token
 * get pageination
 * return query tokens, numOfdocs, suggestions, search operation time
 * return snippets, url, website title, page title of every doc
 * 
 * separate functionalities:
 * 1. AND/OR/NOT
 * 2. Phrase search
 *
 * TODO:
 * 1. create the interface of the API
 * 2. get suggestions -> Endpoint (GET /suggestions)
 * 3. post search query -> Endpoint (POST /search)
 * 4. post pagination -> Endpoint (POST /page)
 * 6. get all results metadata -> middleware
 * 7. get all results info -> middleware
 * 8. preprocess the query -> middleware
 * 9. search in the indexer and get all docs -> middleware
 * 10. rank the docs -> middleware
 * 11. Save all docs in a list -> middleware
 *
 */

@Path("/")
public class queryEngine {
  private Tokenizer tokenizer;

  @GET
  @Path("/suggestions")
  public String getSuggestions(
      @QueryParam("query") String query) {

    List<String> tokens = tokenizer.tokenizeString(query);
    return "Suggestions";
  }

  @POST
  @Path("/search")
  public String searchQuery(
      @QueryParam("query") String query) {

    List<String> tokens = tokenizer.tokenizeString(query);
    return "Search Query";
  }

  @POST
  @Path("/page")
  public String changePagination() {
    return "Pagination";
  }

  public String getResultsMetadata() {
    return "Results Metadata";
  }

  public String getResultsInfo() {
    return "Results Info";
  }

  public void searchIndexer() {
    System.out.println("Search Indexer");
  }

  public void rankDocs() {
    System.out.println("Rank Docs");
  }

  public void saveCurrentDocs() {
    System.out.println("Save Docs");
  }
}
