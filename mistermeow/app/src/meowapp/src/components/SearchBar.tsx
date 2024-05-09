import CloseIcon from "@/assets/icons/CloseIcon";
import SearchIcon from "@/assets/icons/SearchIcon";
import { fetchSuggestions } from "@/utils/suggestion-api";
import { useQuery } from "react-query";
import { useComponentVisible } from "@/utils/hooks";

export interface SearchBarProps {
  query: string;
  setQuery: (query: string) => void;
  className?: string;
}

function SearchBar({ query, setQuery, className }: SearchBarProps) {
  const { data } = useQuery(
    ["suggestions", query],
    () => fetchSuggestions(query),
    {
      enabled: query.length > 0,
    }
  );

  const {
    ref,
    isComponentVisible: isFocused,
    setIsComponentVisible,
  } = useComponentVisible(true);

  return (
    <div
      ref={ref}
      className={
        "bg-search border border-searchBorder rounded-[22px] lg:min-w-[585px] md:min-w-[585px] sm:min-w-full cursor-pointer select-text mt-5 " +
        className
      }
    >
      <div className="min-h-[44px] flex place-items-center">
        <SearchIcon className="ml-4 select-none cursor-default" />
        <input
          className="flex-grow pl-4 pr-4 bg-search border-none rounded-full focus:outline-none outline-none"
          type="text"
          placeholder="Search for anything"
          value={query}
          onChange={(e) => {
            setQuery(e.target.value);
          }}
          onFocus={() => setIsComponentVisible(true)}
        />
        {query && (
          <span onClick={() => setQuery("")}>
            <CloseIcon className="mr-5 select-none cursor-pointer" />
          </span>
        )}
      </div>
      {query && data && isFocused && (
        <div>
          <hr className={`border-t border-searchBorder w-11/12 m-auto mb-2 `} />
          <div className="flex flex-col gap-2 pl-4 pr-4 pb-2">
            {data.map((suggestion: any) => (
              <div
                key={suggestion}
                className="flex align-middle place-items-center p-1 hover:rounded hover:bg-gray-50 dark:hover:bg-gray-700"
                onClick={() => {
                  setQuery(suggestion);
                }}
              >
                <SearchIcon className="mr-2" />
                <div>{suggestion}</div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default SearchBar;
