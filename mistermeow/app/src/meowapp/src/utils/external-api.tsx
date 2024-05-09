async function fetchTags() {
  const response = await fetch(
    "https://api.github.com/repos/amir-kedis/mister-meow/tags"
  );
  if (!response.ok) {
    throw new Error("Failed to fetch tags");
  }
  const tags = await response.json();
  return tags;
}

export { fetchTags };
