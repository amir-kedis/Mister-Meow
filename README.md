<h1 align="center">
    <img src="./docs/imgs/MISTER_MEOW.png" />
    <br />
</h1>

<h1 align="center">
    🐈 Mister Meow 🐈
</h1>

## 📹 Demo

https://github.com/amir-kedis/Mister-Meow/assets/88613195/fb2a0634-326d-41d2-bda4-1d5d5c815f54

---

## ✨ Features

- **MeowCrawler**: crawl the web and insert the data into the database.
  - multi-threading
  - multi level host priority queue
  - handles robots.txt
  - url hashing and content hashing to prevent duplicate content
  - url filtering
  - url normalization
  - seeding with a list of urls
  - Incremental crawling - could be paused and resumed
  - creates a sitemap graph for the ranking algorithm
- **MeowIndexer**: tokenize and index the crawled data.
  - multi-threading
  - store in a inverted index collection
  - get the TF and position of the tokens.
  - handles stemming **(Porter Stemmer)** PS: we are required to give higher priority to exact tokens\_
  - handles stop words
  - incremental indexing - could be paused and resumed
- **MeowRanker**: search the indexed data.
  - search for the query in the inverted index
  - use Google Page Rank algorithm to give popularity to the pages
  - rank the results based on the TF-IDF algorithm
  - phrase matching
  - higher rank bonus for the exact match then stems
  - higher rank bonus for words in important tags like title, h1, h2, etc.
- **MeowEngine**: query engine and server.
  - RESTful API
  - snippet generation
  - search suggestions and history
  - query parsing
  - phrase matching queries
  - AND, OR, NOT operators in queries
  - stop words and stemming
  - pagination
  - cache
- **MeowApp**: web application.
  - Fancy Custom theming 4 themes are available (light, dark, rose, and black)
  - Powerful Search bar and suggestions components
  - fancy pagination element
  - navigation and data loading with react-router 6

---

## 📈 Performance

> [!IMPORTANT]
>
> 1. 🕷 Crawler: **1000** pages in **1m12s** with 64 threads
> 2. 📓 Indexer: **1000** pages in **47s** with 50 threads
> 3. 🔎 Search: search is not stable enough but in general it could be improved in the ranker.

---

## System Design

### Basic System component

![System Design](./docs/imgs/draft-system-design.png)

### Indexer DB Design

![indexer DB Design](./docs/imgs/IndexerDB.excalidraw.png)

### Build Inverted Index Algorithm

![Build Index](./docs/imgs/build-the-index-algo.excalidraw.png)

## Contributions

Please check the following documents before contributing:

- [Git flow](/docs/conventions/git-flow.md)
- [Java setup](/docs/conventions/java-env.md)
- [monogo setup](/docs/conventions/mongo.md)
