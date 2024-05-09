import SearchBar from "./components/SearchBar.tsx";
import Footer from "@/components/Footer.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext, useState } from "react";
import Banner from "./components/Banner.tsx";
import { useQuery } from "react-query";
import { fetchResults } from "./utils/results-api.tsx";

interface ThemeContextType {
  theme: string;
}

export interface SRPProps {
  initQuery?: string;
  page?: number;
}

function SRP({ initQuery, page = 1 }: SRPProps) {
  const { theme } = useContext(ThemeContext) as unknown as ThemeContextType;
  const [query, setQuery] = useState(initQuery || "");
  const { data, isLoading, isError } = useQuery(["search", query, page], () =>
    fetchResults(query, page)
  );

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
      <div className="flex-grow container">
        {isLoading && (
          <div className="flex place-items-center text-center justify-center font-bilya">
            Loading...
          </div>
        )}
        {isError && (
          <div className="flex place-items-center text-center justify-center font-bilya">
            Error fetching results
          </div>
        )}

        {!isLoading && !isError && data && data.results?.length == 0 && (
          <div className="flex place-items-center text-center justify-center font-bilya">
            No results found
          </div>
        )}

        {!isLoading && !isError && data && (
          <div>
            <h6 className="font-inder mt-1 text-caption text-sm ">
              Meow Found about {data.count.toLocaleString()} results
            </h6>
            <div className="mt-1 flex no-wrap gap-4 place-items-center">
              {data.tags.map((tag) => (
                <span
                  key={tag}
                  className="text-sm text-sr-tag bg-sr-tag pl-4 pr-4 pt-2 pb-2 rounded-full border border-sr-tag"
                >
                  {tag}
                </span>
              ))}
            </div>
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default SRP;
