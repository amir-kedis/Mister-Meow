import SearchBar from "./components/SearchBar.tsx";
import Footer from "@/components/Footer.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext, useState } from "react";
import Banner from "./components/Banner.tsx";

interface ThemeContextType {
  theme: string;
}

export interface SRPProps {
  searchResults: {
    results: any[];
    count: number;
    tags: string[];
    suggestions: string[];
  };
  page: number;
}

function SRP({ searchResults, page }: SRPProps) {
  // FIXME: dump conversion learn to make the correct way
  const { theme } = useContext(ThemeContext) as unknown as ThemeContextType;
  const [query, setQuery] = useState("");

  return (
    <div
      className={` ${theme} flex flex-col bg-home text-primary h-screen font-inter fill-current`}
    >
      <nav className="flex pt-4 pb-4 border-searchBorder border-b">
        <div className="container flex gap-3">
          <Banner size="sm" />
          <SearchBar
            className="mt-0 inline-block"
            query={query}
            setQuery={setQuery}
          />
        </div>
      </nav>
      <div className="flex-grow"> </div>
      <Footer />
    </div>
  );
}

export default SRP;
