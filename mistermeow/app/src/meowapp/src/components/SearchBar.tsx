import { useState } from "react";

export interface SearchBarProps {
  query: string;
  setQuery: (query: string) => void;
}

function SearchBar({ query, setQuery }: SearchBarProps) {
  return (
    <div className="min-h-[44px] bg-search border mt-5 rounded-full min-w-[585px] cursor-pointer select-text flex">
      <input
        className="flex-grow pl-4 pr-4 bg-search border-none rounded-full focus:outline-none"
        type="text"
        placeholder="Search for anything"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
    </div>
  );
}

export default SearchBar;
