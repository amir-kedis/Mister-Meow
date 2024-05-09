import Home from "./home";

import { ThemeProvider } from "./contexts/themeContext";
import SRP from "./SRP";

function App() {
  return (
    <>
      <ThemeProvider>
        <div className="dark rose black hidden"></div>
        {/* <Home /> */}
        <SRP />
      </ThemeProvider>
    </>
  );
}

export default App;
