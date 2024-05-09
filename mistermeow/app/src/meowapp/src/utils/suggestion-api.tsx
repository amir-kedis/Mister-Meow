async function fetchSuggestions(query: string) {
  return Promise.resolve([
    "meow",
    "Cats are the best",
    "Cats Loves Mister Meow",
    "Mister Meow is the best",
    "Cats are better than you so you are not the best",
    "Cats: the history search will be silly",
  ]);
  const response = await fetch(`mister-meow.com/suggestions?q=${query}`);
  if (!response.ok) {
    // throw new Error("Failed to fetch suggestions");
    return {
      suggestions: [
        "meow",
        "Cats are the best",
        "Cats Loves Mister Meow",
        "Mister Meow is the best",
        "Cats are better than you so you are not the best",
        "Cats: the history search will be silly",
      ],
    };
  }
  const suggestions = await response.json();
  return suggestions;
}

export { fetchSuggestions };
