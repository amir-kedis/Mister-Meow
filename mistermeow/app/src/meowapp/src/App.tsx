import Home from "./home";

import { ThemeProvider } from "./contexts/themeContext";

function App() {
  return (
    <>
      <ThemeProvider>
        <div className="dark rose black hidden"></div>
        <Home />
      </ThemeProvider>
    </>
  );
}

export default App;
