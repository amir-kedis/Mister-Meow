import SearchBar from "./components/SearchBar.tsx";
import Footer from "./components/Footer.tsx";
import Banner from "./components/Banner.tsx";
import { ThemeContext } from "./contexts/themeContext.tsx";
import { useContext } from "react";

function Home() {
  const { theme, setTheme } = useContext(ThemeContext);
  return (
    <div
      className={` ${theme} flex flex-col bg-home text-primary h-screen fill-current`}
    >
      <div className="flex-grow flex flex-col justify-center items-center">
        <Banner />
        <SearchBar />
      </div>
      <Footer />
    </div>
  );
}

export default Home;
