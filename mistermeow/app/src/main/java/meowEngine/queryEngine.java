package meowEngine;

import javax.ws.rs.*;

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
 * 5. get all results metadata -> Endpoint (GET /metaResults)
 * 6. get all results info -> Endpoint (GET /results)
 * 7. preprocess the query -> middleware
 * 8. search in the indexer and get all docs -> middleware
 * 9. rank the docs -> middleware
 * 10. Save all docs in a list -> middleware
 *
 */

@Path("/")
public class queryEngine {

  @GET
  @Path("/suggestions")
  public String getSuggestions() {
    return "Suggestions";
  }

  @POST
  @Path("/search")
  public String searchQuery() {
    return "Search Query";
  }

  @POST
  @Path("/page")
  public String changePagination() {
    return "Pagination";
  }

  @GET
  @Path("/metaResults")
  public String getResultsMetadata() {
    return "Results Metadata";
  }

  @GET
  @Path("/results")
  public String getResultsInfo() {
    return "Results Info";
  }

  public void preprocessQuery() {
    System.out.println("Preprocess Query");
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
