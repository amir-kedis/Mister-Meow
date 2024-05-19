import SearchBar from "./components/SearchBar.tsx";
import Footer from "@/components/Footer.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext, useState } from "react";
import Banner from "./components/Banner.tsx";
import { fetchResults } from "./utils/results-api.tsx";
import CatIcon from "./assets/icons/CatIcon.tsx";
import { Link, useLoaderData, useNavigate, Form } from "react-router-dom";
import { Results } from "./utils/results-api.tsx";
import SRPPagination from "./components/SRPPagination.tsx";

export async function loader({
  params = { query: "", page: 1 },
}): Promise<{ data: Results; query: string; page: number; fetchTime: number }> {
  const { query, page } = params;
  console.log("Query", query);
  console.log("Page", page);

  const startTime = Date.now();
  const data = await fetchResults(query, page);
  const fetchTime = Date.now() - startTime;
  return { data, query, page, fetchTime };
}

interface ThemeContextType {
  theme: string;
}

export interface SRPProps {
  initQuery?: string;
  page?: number;
}

function SRP() {
  const {
    data,
    query: initQuery,
    page: initPage,
    fetchTime,
  } = useLoaderData() as {
    data: Results;
    query: string;
    page: number;
    fetchTime: number;
  };
  const { theme } = useContext(ThemeContext) as unknown as ThemeContextType;
  const [query, setQuery] = useState(initQuery || "");
  const [page, setPage] = useState(initPage || 1);
  const navigate = useNavigate();

  console.log(data);

  const submitHandler = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log("Search button clicked");
    if (query.trim() === "") {
      return;
    }
    navigate(`/search/${query}/1`);
  };

  return (
    <div
      className={` ${theme} flex flex-col bg-home text-mprimary min-h-screen font-inter fill-current`}
    >
      <nav className="flex pt-4 pb-4 border-searchBorder max-h-[78px] relative border-b">
        <div className="container flex gap-3 ">
          <Link to="/">
            <Banner size="sm" />
          </Link>
          <Form onSubmit={submitHandler}>
            <SearchBar
              className="mt-0 inline-block z-[1000]"
              query={query}
              setQuery={setQuery}
            />
          </Form>
        </div>
      </nav>
      <div className="flex-grow container">
        {data && (
          <div>
            <h6 className="font-inder mt-3 mb-3 text-caption text-sm ">
              Meow Found about <strong>{data.count.toLocaleString()}</strong>{" "}
              results in <strong>{(fetchTime / 1000).toLocaleString()}s</strong>
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
                {data && data.results?.length == 0 && (
                  <div className="flex place-items-center text-center justify-center min-h-[60vh] font-bilya">
                    No results found
                  </div>
                )}

                {data.results.map(
                  (result) =>
                    result.snippets && (
                      <div className="mb-6" key={result.URL}>
                        <a href={result.URL} target="_blank">
                          <div className="flex gap-2">
                            <CatIcon />
                            <div className="flex flex-col ">
                              <span className="text-sm text-sr-host leading-tight">
                                {result.host}
                              </span>
                              <span className="text-sm truncate max-w-[70ch] text-sr-url leading-tight hover:underline">
                                {result.URL}
                              </span>
                            </div>
                          </div>
                          <h3 className="text-sr-title text-xl hover:underline">
                            {result.title}
                          </h3>
                          <p className="text-sr-snippet leading-tight">
                            {result.snippets
                              .split("*")
                              .map((snippet, index) => (
                                <>
                                  {index == 1 ? (
                                    <strong>{snippet}</strong>
                                  ) : (
                                    snippet
                                  )}
                                </>
                              ))}
                          </p>
                        </a>
                      </div>
                    ),
                )}
                <SRPPagination
                  page={Number(page)}
                  query={query}
                  resultsCount={data.count}
                  setPage={setPage}
                />
              </div>
              <div>
                <div className="flex sticky top-6 flex-col gap-2 rounded-xl bg-search border p-4 drop-shadow-sm shadow-searchShadow">
                  <h6 className="text-base font-bold">Related Searches</h6>
                  {data.suggestions.map((suggestion) => (
                    <a
                      key={suggestion}
                      onClick={() => {
                        setQuery(suggestion);
                        navigate(`/search/${suggestion}/1`);
                      }}
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
