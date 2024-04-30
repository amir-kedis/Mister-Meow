import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { Button } from "./components/ui/button";
import { BeakerIcon } from "@heroicons/react/24/solid";
import { useQuery } from "react-query";

function App() {
  const [count, setCount] = useState(0);

  const QoutesSection = function () {

    const { data, isLoading, error } = useQuery("qoutes", async () => {
      const response = await fetch("https://api.quotable.io/random");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    });

    if (isLoading) return "Loading...";
    if (error) return "An error has occurred: " + error;
    return (
      <div>
        <h1>{data.content}</h1>
        <p>{data.author}</p>
      </div>
    );
  };

  return (
    <>
      <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
        <h1 className="text-3xl underline font-bold text-red-500">
          Hello world!
        </h1>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
        <Button variant="secondary" size="sm">
          <BeakerIcon className="h-5 w-5 mr-2" />
          Read the docs
        </Button>
      </p>
      <QoutesSection />
    </>
  );
}

export default App;
