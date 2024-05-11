package meowEngine;

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
 * 2. get suggestions -> Endpoint
 * 3. post search query -> Endpoint
 * 4. post pagination -> Endpoint
 * 5. get all results metadata -> Endpoint
 * 6. get all results into -> Endpoint
 * 7. preprocess the query -> middleware
 * 8. search in the indexer and get all docs -> middleware
 * 9. rank the docs -> middleware
 * 10. Save all docs in a list -> middleware
 *
 */
