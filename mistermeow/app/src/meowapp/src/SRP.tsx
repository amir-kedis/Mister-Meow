import SearchBar from "./components/SearchBar.tsx";
import Footer from "@/components/Footer.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext, useState } from "react";
import Banner from "./components/Banner.tsx";
import { useQuery } from "react-query";
import { fetchResults } from "./utils/results-api.tsx";
import CatIcon from "./assets/icons/CatIcon.tsx";

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
      className={` ${theme} flex flex-col bg-home text-primary min-h-screen font-inter fill-current`}
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
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
              <div className="col-span-2">
                {data.results.map((result) => (
                  <div className="mb-4" key={result.url}>
                    <a href={result.url} target="_blank" rel="noreferrer">
                      <div className="flex gap-2">
                        <CatIcon />
                        <div className="flex flex-col ">
                          <span className="text-sm text-sr-host leading-tight">
                            {result.host}
                          </span>
                          <span className="text-sm text-sr-url leading-tight hover:underline">
                            {result.url}
                          </span>
                        </div>
                      </div>
                      <h3 className="text-sr-title text-xl hover:underline">
                        {result.title}
                      </h3>
                      <p className="text-sr-snippet leading-tight">
                        {result.snippets}
                      </p>
                    </a>
                  </div>
                ))}
              </div>
              <div>
                <div className="flex flex-col gap-2">
                  <h6 className="text-base text-sr-tag font-bold">
                    Related Searches
                  </h6>
                  {data.suggestions.map((suggestion) => (
                    <a
                      key={suggestion}
                      onClick={() => setQuery(suggestion)}
                      href="#"
                      className="text-sm hover:underline"
                    >
                      {suggestion}
                    </a>
                  ))}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default SRP;
