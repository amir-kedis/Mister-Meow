import SearchBar from "./components/SearchBar.tsx";
import Footer from "./components/Footer.tsx";
import Banner from "./components/Banner.tsx";

function Home() {
  return (
    <div className="flex flex-col h-screen">
      <div className="flex-grow flex flex-col justify-center items-center">
        <Banner />
        <SearchBar />
      </div>
      <Footer />
    </div>
  );
}

export default Home;
