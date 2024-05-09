import SearchBar from "./components/SearchBar.tsx";
import Footer from "./components/Footer.tsx";
import Banner from "./components/Banner.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext } from "react";

function Home() {
  const { theme } = useContext(ThemeContext);

  const submitHandler = (e) => {
    e.preventDefault();
    console.log("Search button clicked");
  };

  return (
    <div
      className={` ${theme} flex flex-col bg-home text-primary h-screen fill-current`}
    >
      <div className="flex-grow flex flex-col justify-center items-center">
        <Banner />
        <form onSubmit={submitHandler}>
          <SearchBar />
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
