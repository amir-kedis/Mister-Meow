async function fetchResults(query: string, page: number) {
  return Promise.resolve({
    results: [
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
      {
        host: "CatTime",
        url: "https://cattime.com/cat-breeds",
        title: "Cat Breeds - Types of Cats - CatTime",
        snippets:
          "List of Cat Breeds. Abyssinian. Compare breed Read Breed Profile. Aegean. Compare breed Read Breed Profile. American Bobtail. Read Breed Profile. American Curl. …",
      },
    ],
    count: 3200420,
    tags: ["Cats", "Kittens", "Meow"],
    suggestions: [
      "meow",
      "Cats are the best",
      "Cats Loves Mister Meow",
      "Mister Meow is the best",
      "Cats are better than you so you are not the best",
      "Cats: the history search will be silly",
    ],
  });
  const response = await fetch(
    `https://mister-meow.com/results?q=${query}&page=${page}`
  );
}

export { fetchResults };
