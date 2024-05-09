import SearchBar from "./components/SearchBar.tsx";
import Footer from "./components/Footer.tsx";
import Banner from "./components/Banner.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext, useState } from "react";

interface ThemeContextType {
  theme: string;
}

function Home() {
  // FIXME: dump conversion learn to make the correct way
  const { theme } = useContext(ThemeContext) as unknown as ThemeContextType;
  const [query, setQuery] = useState("");

  const submitHandler = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log("Search button clicked");
    // TODO: GO to SRP and give it the results
  };

  return (
    <div
      className={` ${theme} flex flex-col bg-home text-primary h-screen font-inter fill-current`}
    >
      <div className="flex-grow flex flex-col justify-center items-center">
        <Banner />
        <form
          className="flex flex-col place-items-center gap-1"
          onSubmit={submitHandler}
        >
          <SearchBar query={query} setQuery={setQuery} className="mt-5" />
          <button
            type="submit"
            className="mt-4 bg-btn text-btn font-inter font-semibold text-base rounded pl-4 pr-4 pt-2 pb-2 hover:filter hover:saturate-50 transition-colors duration-300 ease-in-out focus:outline-none focus:ring-2 focus:ring-primary focus:ring-opacity-50"
          >
            Meow Search
          </button>
        </form>
      </div>
      <Footer />
    </div>
  );
}

export default Home;
