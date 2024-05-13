async function fetchSuggestions(query: string) {
  // return Promise.resolve([
  //   "meow",
  //   "Cats are the best",
  //   "Cats Loves Mister Meow",
  //   "Mister Meow is the best",
  //   "Cats are better than you so you are not the best",
  //   "Cats: the history search will be silly",
  // ]);
  const response = await fetch(
    `http://localhost:8080/suggestions?query=${query}`,
  );
  // if (!response.ok) {
  //   // throw new Error("Failed to fetch suggestions");
  //   return {
  //     suggestions: [
  //       "meow",
  //       "Cats are the best",
  //       "Cats Loves Mister Meow",
  //       "Mister Meow is the best",
  //       "Cats are better than you so you are not the best",
  //       "Cats: the history search will be silly",
  //     ],
  //   };
  // }

  let suggestions = await response.json();
  suggestions = suggestions.data;
  console.log(suggestions);
  return suggestions;
}

export { fetchSuggestions };
